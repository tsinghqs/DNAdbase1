import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * @author vpratha
 * @version 5.7.2019
 * 
 * 
 * 
 */
public class MemoryManager          
{
    private RandomAccessFile memoryFile;
    /**
     * Offset in bytes for EOF in memoryFile.
     */
    //private int eof;
    private LinkedList<Handle> freelist;
    
    /**
     * Memory Manager class
     * @param memFile the random access file
     */
    public MemoryManager(RandomAccessFile memFile)
    {
        memoryFile = memFile;
        //eof = 0;
        freelist = new LinkedList<Handle>();
    }
    
    /**
     * Stores item, the sequence or sequenceID
     * string, in binary representation to memoryFile,
     * and returns a Handle to that item.
     * @param item the sequence or sequenceID to be stored
     * @return a Handle to that item
     * @throws IOException 
     */
    public Handle storeItem(String item) throws IOException
    {
        int length = item.length();
        
        byte[] rep = getBinaryRep(item);
        //int numBytes = rep.length;
        int offset = firstFit(item);
        //if (offset == eof)
//        if (offset == memoryFile.length())
//        {
//            memoryFile.seek(memoryFile.length());
//            memoryFile.write(rep);
//        }
//        else
//        {
//            memoryFile.write(rep, offset, numBytes);
//        }
        
        //eof = (int)memoryFile.length();
        
        memoryFile.seek(offset);
        memoryFile.write(rep);
        
        Handle itemHandle = new Handle(offset, length);
        return itemHandle;
    }
    
    /**
     * Gets the offset of the first free block
     * in freelist that can accommodate item;
     * updates that block's offset and length.
     * If there are no free blocks that qualify, 
     * returns eof.
     * @param item the sequence or sequenceID to be stored
     * @return the first valid offset where item can be stored
     *         in memoryFile
     * @throws IOException 
     */
    public int firstFit(String item) throws IOException
    {
        int offset = (int) memoryFile.length();
        
        if (freelist.size() == 0)
        {
            return offset;
        }
        
//        for (Handle freeBlock : freelist)
//        {
//            if (freeBlock.getLength() >= item.length())
//            {
//                offset = freeBlock.getOffset();
//                freeBlock.setOffset(offset + getNumBytes(item));
//                freeBlock.setLength(freeBlock.getLength() - item.length());
//                if (freeBlock.getLength() == 0)
//                {
//                    freelist.remove(freeBlock);
//                }
//                return offset;
//            }
//        }
        
        for (int i = 0; i < freelist.size(); i++)
        {
            Handle freeBlock = freelist.get(i);
            if (freeBlock.getLength() >= item.length())
            {
                offset = freeBlock.getOffset();
                freeBlock.setOffset(offset + getNumBytes(item));
                freeBlock.setLength(freeBlock.getLength() - item.length());
                if (freeBlock.getLength() == 0)
                {
                    freelist.remove(freeBlock);
                }
                return offset;
            }
        }
        
        return offset;
    }
    
    /**
     * Removes sequenceID and sequence of
     * target. Does not physically delete
     * their respective bytes in memoryFile,
     * but treats their allocated memories
     * as free blocks. Decreases the length
     * of memoryFile
     * @param target the record containing
     *        the handles to the items to 
     *        be removed
     * @throws IOException 
     */
    public void remove(Record target) throws IOException
    {
        remove(target.getSeqIDHandle());
        remove(target.getSeqHandle());
    }
    
    /**
     * Removes item. Does not physically delete
     * its respective bytes in memoryFile,
     * but treats its allocated memory
     * as a free block.
     * @param itemHandle the handle of the
     *                   the item to be 
     *                   removed
     * @throws IOException 
     */
    public void remove(Handle itemHandle) throws IOException
    {
        int itemOffset = itemHandle.getOffset();
        int position = 0;
//        for (int i = 0; i < freelist.size() - 1; i++)
//        {
//            if (freelist.get(i).getOffset() < itemOffset 
//                && freelist.get(i + 1).getOffset() > itemOffset)
//            {
//                position = i + 1;
//                break;
//            }
//        }
        
        while (position < freelist.size() 
            && freelist.get(position).getOffset() < itemOffset)
        {
            position++;
        }
        freelist.add(position, itemHandle);
        
        //System.out.println("Freeblock added!");
        //printFreelist();
        
        updateFreelist();
        
        //System.out.println("Freelist updated!");
        //printFreelist();
    }
    
    /**
     * Merges any adjacent free blocks.
     * If the last free block is adjacent
     * to EOF, that block is removed from
     * freelist, and the length of 
     * memoryFile is decremented accordingly.
     * @throws IOException 
     */
    public void updateFreelist() throws IOException
    {
        // merge adjacent free blocks
        for (int i = 0; i < freelist.size() - 1; i++)
        {
            Handle thisBlock = freelist.get(i);
            Handle nextBlock = freelist.get(i + 1);
            if (isAdjacent(thisBlock, nextBlock))
            {
                thisBlock.setBytes(thisBlock.getBytes() 
                    + nextBlock.getBytes());
                freelist.remove(i + 1);
                i = -1;
            }
        }
        
        // decrement memoryFile size if needed
        Handle lastBlock = freelist.peekLast();
        int offset = lastBlock.getOffset();
        int bytes = getNumBytes(lastBlock.getLength());
        if (offset + bytes == memoryFile.length())
        {
            freelist.removeLast();
            memoryFile.setLength(memoryFile.length() - bytes);
        }
    }
        
    /**
     * Determines whether two consecutive blocks 
     * in freelist are adjacent in memory.
     * @param freeblock1 the first free block
     * @param freeblock2 the second free block
     * @return whether two free blocks are adjacent
     */
    public boolean isAdjacent(Handle freeblock1, Handle freeblock2)
    {
        int offset1 = freeblock1.getOffset();
        //int fb1bytes = getNumBytes(freeblock1.getLength());
        int bytes1 = freeblock1.getBytes();
        int offset2 = freeblock2.getOffset();
        return (offset1 + bytes1) == offset2;
    }
    
    /**
     * Method to print the free space
     */
    public void printFreelist()
    {
        for (int i = 0; i < freelist.size(); i++)
        {
            Handle curr = freelist.get(i);
            System.out.printf("[Block %d] Starting Byte" + 
                " Location: %d, Size %d bytes\n", 
                i + 1, curr.getOffset(), curr.getBytes());
        }
    }
    
    /**
     * Converts item, the sequence or sequenceID
     * string, to a byte[]. Each byte in the byte[]
     * will represent 4 characters in item, with
     * each character having a 2-bit representation:
     * A = 00
     * C = 01
     * G = 10
     * T = 11.
     * If the length of item is not divisible by 4,
     * then the remaining bits of the last byte in the 
     * byte[] will be 0s.
     * @param item the sequence or sequenceID to be stored
     * @return The byte[] representation of item
     */
    public byte[] getBinaryRep(String item)
    {   
        char[] itemChars = item.toCharArray();

        int numBytes = itemChars.length / 4;
        int fillerLetters = 0;
        if (item.length() % 4 != 0) {
            numBytes++;
            fillerLetters += 4 - (item.length() % 4);
        }

        // System.out.println("numBytes: " + numBytes);

        byte[] rep = new byte[numBytes];

        int currByte = 0;
        int remainingLettersInCurrByte = 4;
        int canvas = 0;
        for (int i = 0; i < itemChars.length + fillerLetters; i++) {
            int letterBits = 0;

            if (i < itemChars.length) {
                char letter = itemChars[i];

                if (letter == 'C') {
                    letterBits = 1;
                }
                else if (letter == 'G') {
                    letterBits = 2;
                }
                else if (letter == 'T') {
                    letterBits = 3;
                }
            }

            // System.out.println("letterBits: " + letterBits);

            // canvas = canvas | letterBits;
            // canvas = canvas << (2 * (remainingLettersInCurrByte - 1));
            int shiftAmt = 2 * (remainingLettersInCurrByte - 1);
            canvas += (letterBits * (int)Math.pow(2, shiftAmt));

            // System.out.println("canvas: " + canvas);

            remainingLettersInCurrByte--;

            if (remainingLettersInCurrByte == 0) {
                rep[currByte] = (byte)(canvas); // & 0xFF);
                currByte++;
                canvas = 0;
                remainingLettersInCurrByte = 4;
            }
        }

        return rep;
    }
    
    /**
     * Returns the number of bytes required
     * to store item in memoryFile.
     * @param item the sequence or sequenceID to be stored
     * @return the number of bytes required
     *         to store item in memoryFile
     */
    public int getNumBytes(String item)
    {
        char[] itemChars = item.toCharArray();
        
        int numBytes = itemChars.length / 4;
        if (item.length() % 4 != 0)
        {
            numBytes++;
        }
        
        return numBytes;
    }
    
    /**
     * Returns the number of bytes required
     * to store any item in memoryFile with
     * length len.
     * @param len the number of characters
     * @return the number of bytes required
     *         to store any item in memoryFile 
     *         with length len
     */
    public int getNumBytes(int len)
    {        
        int numBytes = len / 4;
        if (len % 4 != 0)
        {
            numBytes++;
        }
        return numBytes;
    }
    
    /**
     * Gets string from a handle offset
     * 
     * @param binRep input handle
     * @return String representation of Handle bytes
     * @throws IOException If can't seek
     */
    public String getHandleString(Handle binRep) throws IOException {
        StringBuilder strn = new StringBuilder();
        int num = 0;
        int off = binRep.getOffset();
        int len = binRep.getLength();
        if (len % 4 != 0) {
            num = 1;
        }
        num += len / 4;
        byte[] bytes = new byte[num];
        memoryFile.seek(off);
        memoryFile.read(bytes);
        int appCount = 0;
        for (int i = 0; i < num; i++) {
            String s1 = String.format("%8s", Integer.toBinaryString(bytes[i]
                & 0xFF)).replace(' ', '0');
            for (int j = 0; j < 8; j += 2) {
                String check = s1.substring(j, j + 2);
                if (check.equals("00")) {
                    strn.append("A");
                }
                else if (check.equals("01")) {
                    strn.append("C");
                }
                else if (check.equals("10")) {
                    strn.append("G");
                }
                else if (check.equals("11")) {
                    strn.append("T");
                }
                appCount++;
                if (appCount == len) {
                    break;
                }
            }
            if (appCount == len) {
                break;
            }
        }
        return strn.toString();

    }
    
    /**
     * Method to return list of free spots
     * @return the freeList array
     */
    public LinkedList<Handle> getFreelist()
    {
        return freelist;
    }
}
