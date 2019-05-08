/**
 * @author vpratha
 * @version 5.7.2019
 * 
 * 
 * Record stores two memory handles:
 * the memory handle for a sequenceID, and
 * the memory handle for its corresponding sequence.
 */
public class Record 
{
    /**
     * The handle for the sequenceID.
     */
    private Handle seqIDHandle;
    /**
     * The handle for the sequence.
     */
    private Handle seqHandle;
    private boolean tombstone;
    
    /**
     * Record's constructor.
     * @param seqIDH the handle for the sequenceID
     * @param seqH the handle for the sequence
     */
    public Record(Handle seqIDH, Handle seqH)
    {
        seqIDHandle = seqIDH;
        seqHandle = seqH;
        tombstone = false;
    }

    /**
     * Returns the sequenceID handle.
     * @return the seqIDHandle
     */
    public Handle getSeqIDHandle() {
        return seqIDHandle;
    }

    /**
     * Sets the sequenceID handle.
     * @param seqIDHandle the seqIDHandle to set
     */
    public void setSeqIDHandle(Handle seqIDHandle) {
        this.seqIDHandle = seqIDHandle;
    }

    /**
     * Returns the sequence handle.
     * @return the seqHandle
     */
    public Handle getSeqHandle() {
        return seqHandle;
    }
    
    /**
     * Method to make a record into tombstone
     */
    public void makeTombstone()
    {
        this.tombstone = true;
    }
    
    /**
     * Method to check tombstone
     * @return whether or not the record is a tombstone
     */
    public boolean isTombstone() {
        return this.tombstone;
    }
    /**
     * Sets the sequence handle.
     * @param seqHandle the seqHandle to set
     */
    public void setSeqHandle(Handle seqHandle) {
        this.seqHandle = seqHandle;
    }
    

}
