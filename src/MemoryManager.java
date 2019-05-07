
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class MemoryManager {
    
    private FreeList free_list = new FreeList();
    private RandomAccessFile raf;
    
    public MemoryManager(String filename) throws FileNotFoundException, IOException {
        raf = new RandomAccessFile(filename, "rwd");
        raf.setLength(0);
    }
    
    public Handle insert(String s) throws IOException {
        byte[] bytes = DNASequence.encode(s);
        int offset = free_list.find(bytes.length);
        if (offset == -1) {
            offset = (int) raf.length();
        }
        raf.seek(offset);
        raf.write(bytes);
        return new Handle(offset, s.length());
    }
    
    public void remove(Handle handle) {
        int nbytes = (handle.getLength() - 1) / 4 + 1;
        free_list.add(handle.getOffset(), nbytes);
    }
    
    public String get(Handle handle) throws IOException {
        int nbytes = (handle.getLength() - 1) / 4 + 1;
        byte[] bytes = new byte[nbytes];
        raf.seek(handle.getOffset());
        raf.readFully(bytes);
        return DNASequence.decode(bytes, handle.getLength());
    }
    
    public void close() throws IOException {
        raf.close();
    }
    
    public void print() {
        System.out.print("Free Block List:");
        if (free_list.getSize() == 0) {
            System.out.println(" none");
            return;
        }
        System.out.println();
        int i = 1;
        for (FreeSection sec : free_list.getList()) {
            System.out.print("[Block " + i + "] ");
            System.out.print("Starting Byte Location: " + sec.getOffset());
            System.out.println(", Size " + sec.getLength() + " bytes");
        }
    }
}
