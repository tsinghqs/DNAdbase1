import java.io.IOException;
import java.util.LinkedList;


/**
 * Class to execute commands
 * @author tsingh tsinghqs
 * @version 2019
 *
 */
public class CommandExecution {
    
    /**
     * @field Hashtable tab
     */
    private HashTable tab;
    /**
     * @field MemoryManager mem
     */
    private MemoryManager mem;
    /**
     * @field int hashtable size
     */
    private int hashTableSize;
    /**
     * @field numRecords
     */
    private int numRecords = 0;
    /**
     * Constructor for command executioner class
     * @param hashing size of hashtable
     * @param meming memory file
     */
    public CommandExecution(HashTable hashing, MemoryManager meming)
    {
        tab = hashing;
        mem = meming;
        hashTableSize = tab.getHashSize();
    }
    
    /**
     * Method to insert a sequence and sequence id
     * @param sequenceId sequence id
     * @param sequence sequence
     * @throws IOException if there's IO
     */
    public void insert(String sequenceId, String sequence) throws IOException {
        Record insertion = new Record(mem.storeItem(sequenceId), mem.storeItem(
            sequence));
        int hash = (int)this.sfold(sequenceId);
        if (!this.tab.hasKey(insertion)) {
            boolean inserted = this.tab.hashValue(insertion, hash);
            if (inserted) {
                this.numRecords++;
            }
            else {
                mem.remove(insertion);
                System.out.println("Bucket full.Sequence " + sequenceId
                    + " could not be inserted");
            }
        }
        else {
            System.out.println("SequenceID " + sequenceId + " exists");
        }

    }
    
    /**
     * Search method
     * @param id the string we are searching for
     * @throws IOException io exception
     */
    public void search(String id) throws IOException
    {
        String found = this.tab.hasID(id);
        if (!(found.equals("fail"))) {
            System.out.println("Sequence found: " + found);
        }
        else {
            System.out.println("SequenceID " + id + " not found");
        }
    }


    /**
     * Method to remove a hash from the hashtable
     * @param id String to remove
     * @throws IOException IOexception
     */
    public void remove(String id) throws IOException
    {
        boolean found = this.tab.hasStringID(id);
        if (found) {
            // remove vikram
            Record rem = this.tab.removeHash(id);
            mem.remove(rem);
            System.out.println("Sequence removed " + id + ":\n" + this.mem
                .getHandleString(rem.getSeqHandle()));
            this.numRecords--;
        }
        else {
            System.out.println("SequenceID " + id + " not found");
        }
    }
    
    /**
     * Method to print the Values in our database
     * @throws IOException io
     */
    public void print() throws IOException {
        LinkedList<Handle> list = 
            this.mem.getFreelist();
        System.out.println("Sequence IDs:");
        Record[] recs = this.tab.getRecords();
        if (this.numRecords > 0) {
            for (int i = 0; i < this.hashTableSize; i++) {
                if (recs[i] != null && !recs[i].isTombstone()) {
                    Handle seqId = recs[i].getSeqIDHandle();
                    String id = this.mem.getHandleString(seqId);
                    System.out.println(id +
                        ": hash slot [" + i + "]");
                }
            }
        }
        System.out.print("Free Block List:");
        if (list.size() == 0) {
            System.out.println(" none");
        }
        else {
            System.out.println();
            for (int i = 0; i < list.size(); i++) {
                Handle hand = list.get(i);
                System.out.printf("[Block %d] Starting Byte" + 
                    " Location: %d, Size %d bytes\n", 
                    i + 1, hand.getOffset(), hand.getBytes());
            }
        }
    }
    

    /**
     * Method to get hash for a String
     * 
     * @param s
     *            string to be hashed
     * @return the hashed value
     */
    public long sfold(String s) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % this.hashTableSize);
    }

}