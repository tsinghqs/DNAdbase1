
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HashTable {
    
    private int nbuckets;
    private RandomAccessFile raf;
    
    private byte[] bucket = new byte[512];
    
    public HashTable(String filename, int nslots) throws FileNotFoundException, IOException {
        this.nbuckets = nslots / 32;
        raf = new RandomAccessFile(filename, "rwd");
        for (int i = 0; i < 512; i++) {
            bucket[i] = 0;
        }
        for (int i = 0; i < nbuckets; i++) {
            raf.write(bucket);
        }
    }
    
    private Slot slot = new Slot();
    public boolean insert(int slot_num, Handle id, Handle seq) throws IOException {
        loadBucket(slot_num / 32);
        int start = slot_num % 32;
        int index = start;
        while (true) {
            
            // Read the slot at the current position
            slot.read(bucket, index);
            
            // If the slot is empty, then we can insert here
            if (slot.getIdHandle().isEmpty()) {
                new Slot(id, seq).write(bucket, index);
                saveBucket(slot_num / 32);
                return true;
            }
            
            // Go to next slot
            index++;
            
            // If it is out of bucket, cycle from 0
            if (index > 31) {
                index = 0;
            }
            
            // If we reach the "start" again, we are done cycling through
            if (index == start) {
                return false;
            }
        }
    }
    
    public void remove(int slot_num, String id, MemoryManager mem_mgr, boolean verbose) throws IOException {
        loadBucket(slot_num / 32);
        int start = slot_num % 32;
        int index = start;
        while (true) {
            
            // Read the slot at the current position
            slot.read(bucket, index);
            
            // If the slot is not empty,
            if (! slot.getIdHandle().isEmpty()) {
                // and, if the slot contains the same id
                String id_stored = mem_mgr.get(slot.getIdHandle());
                if (id_stored.equals(id)) {
                    // Remove it
                    if (verbose) {
                        System.out.println("Sequence Removed " + id + ":");
                        System.out.println(mem_mgr.get(slot.getSeqHandle()));
                    }
                    
                    // Overwrite hash_table slot with an empty slot
                    new Slot().write(bucket, index);
                    saveBucket(slot_num / 32);
                    
                    // Remove id and seq from memory file
                    mem_mgr.remove(slot.getIdHandle());
                    mem_mgr.remove(slot.getSeqHandle());
                    return;
                }
            }
            
            // Go to next slot
            index++;
            
            // If it is out of bucket, cycle from 0
            if (index > 31) {
                index = 0;
            }
            
            // If we reach the "start" again, we are done cycling through
            if (index == start) {
                break;
            }
        }
        
        if (verbose) {
            System.out.println("Remove Failed: No such sequence!");
        }
    }
    
    public void search(int slot_num, String id, MemoryManager mem_mgr) throws IOException {
        loadBucket(slot_num / 32);
        int start = slot_num % 32;
        int index = start;
        while (true) {
            
            // Read the slot at the current position
            slot.read(bucket, index);
            
            // If the slot is not empty,
            if (! slot.getIdHandle().isEmpty()) {
                // and, if the slot contains the same id
                String id_stored = mem_mgr.get(slot.getIdHandle());
                if (id_stored.equals(id)) {
                    // Print it
                    System.out.println("Sequence Found: " + mem_mgr.get(slot.getSeqHandle()));
                    return;
                }
            }
            
            // Go to next slot
            index++;
            
            // If it is out of bucket, cycle from 0
            if (index > 31) {
                index = 0;
            }
            
            // If we reach the "start" again, we are done cycling through
            if (index == start) {
                break;
            }
        }
        
        System.out.println("SequenceID " + id + " not found");
    }
    
    public boolean contains(int slot_num, String id, MemoryManager mem_mgr) throws IOException {
        loadBucket(slot_num / 32);
        int start = slot_num % 32;
        int index = start;
        while (true) {
            
            // Read the slot at the current position
            slot.read(bucket, index);
            
            // If the slot is not empty,
            if (! slot.getIdHandle().isEmpty()) {
                // and, if the slot contains the same id
                String id_stored = mem_mgr.get(slot.getIdHandle());
                if (id_stored.equals(id)) {
                    return true;
                }
            }
            
            // Go to next slot
            index++;
            
            // If it is out of bucket, cycle from 0
            if (index > 31) {
                index = 0;
            }
            
            // If we reach the "start" again, we are done cycling through
            if (index == start) {
                break;
            }
        }
        
        return false;
    }
    
    public void loadBucket(int index) throws IOException {
        raf.seek(index * 512);
        raf.readFully(bucket);
    }
    
    public void saveBucket(int index) throws IOException {
        raf.seek(index * 512);
        raf.write(bucket);
    }
    
    public void close() throws IOException {
        raf.close();
    }
    
    public void print(MemoryManager mem_mgr) throws IOException {
        
        System.out.println("SequenceIDs:");
        
        for (int i = 0; i < nbuckets; i++) {
            loadBucket(i);
            
            for (int j = 0; j < 32; j++) {
                slot.read(bucket, j);
                if (! slot.getIdHandle().isEmpty()) {
                    String id = mem_mgr.get(slot.getIdHandle());
                    System.out.println(id + ": hash slot [" + (i*32 + j) + "]");
                }
            }
        }
    }
}
