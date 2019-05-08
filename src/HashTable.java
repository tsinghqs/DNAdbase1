import java.io.IOException;

/**
 * Class to create our hashtable
 * @author tsingh tsinghqs
 * @version 2019
 */
public class HashTable {

    private Record[] recs;
    private MemoryManager mems;
    private int size;
    
    /**
     * Hashtable constructor
     * @param size size of hashtable
     * @param mems the memory manager
     */
    public HashTable(int size, MemoryManager mems)
    {
        this.mems = mems;
        recs = new Record[size];
        this.size = size;
    }
    
    /**
     * Method to get the records in a HashTable
     * @return Record[] of records
     */
    public Record[] getRecords()
    {
        return recs;
    }
    
    /**
     * Getter method for size of hashtable
     * @return hashsize the size of hashtable
     */
    public int getHashSize()
    {
        return this.size;
    }
    

    /**
     * Method to add a record to the array
     * 
     * @param hashing The record we are adding
     * @param sfold the computed hash function value
     * @throws IOException io
     * @return whether or not value was hashed
     */
    public boolean hashValue(Record hashing, int sfold) throws IOException {

        if (this.recs[sfold] == null || this.recs[sfold].isTombstone()) {
            recs[sfold] = hashing;
            return true;
        }
        else {
            for (int i = sfold; i < (sfold - sfold % 32) + 32; i++) {
                if (this.recs[i] == null || this.recs[i].isTombstone()) {
                    recs[i] = hashing;
                    return true;
                }
            }
            for (int i = (sfold - sfold % 32); i < sfold; i++) {
                if (this.recs[i] == null || this.recs[i].isTombstone()) {
                    recs[i] = hashing;
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Method to see if key is already in the HashTable
     * 
     * @param compare Record we are comparing to
     * @return boolean if they key is in the table or not
     * @throws IOException io
     */
    public boolean hasKey(Record compare) throws IOException {
        for (int i = 0; i < this.size; i++) {
            if (recs[i] != null && !recs[i].isTombstone()) {
                if (this.mems.getHandleString(recs[i].getSeqIDHandle()).equals(
                    this.mems.getHandleString(compare.getSeqIDHandle()))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Method to see if key is already in the HashTable
     * 
     * @param compare Record we are comparing to
     * @return boolean if they key is in the table or not
     * @throws IOException io
     */
    public boolean hasStringID(String compare) throws IOException {
        for (int i = 0; i < this.size; i++) {
            if (recs[i] != null && !recs[i].isTombstone()) {
                if (this.mems.getHandleString(recs[i].getSeqIDHandle()).equals(
                    compare)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Method to see if key is already in the HashTable
     * 
     * @param compare Record we are comparing to
     * @return boolean if they key is in the table or not
     * @throws IOException io
     */
    public String hasID(String compare) throws IOException {
        for (int i = 0; i < this.size; i++) {
            if (recs[i] != null && !recs[i].isTombstone()) {
                if (this.mems.getHandleString(recs[i].getSeqIDHandle()).equals(
                    compare)) {
                    return this.mems.getHandleString(recs[i].getSeqHandle());
                }
            }
        }
        return "fail";
    }
    
    /**
     * Mehtod to remove a record from hash array
     * @param compare compare to this string
     * @throws IOException io exception
     * @return the record to be removed
     */
    public Record removeHash(String compare) throws IOException
    {
        for (int i = 0; i < this.size; i++) {
            if (recs[i] != null && !recs[i].isTombstone()) {
                if (this.mems.getHandleString(recs[i].getSeqIDHandle()).equals(
                    compare)) {
                    recs[i].makeTombstone();
                    return recs[i];
                }
            }
        }
        return null;
    }
    
    
}
