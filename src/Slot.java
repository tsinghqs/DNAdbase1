
public class Slot {
    private Handle id_handle;
    private Handle seq_handle;
    
    public Slot() {
        id_handle = new Handle();
        seq_handle = new Handle();
    }
    
    public Slot(Handle id_handle, Handle seq_handle) {
        this.id_handle = id_handle;
        this.seq_handle = seq_handle;
    }

    public Handle getIdHandle() {
        return id_handle;
    }

    public Handle getSeqHandle() {
        return seq_handle;
    }
    
    public void read(byte[] bucket, int slot) {
        int index = slot * 16;
        id_handle.setOffset ( getInt(bucket, index +  0) );
        id_handle.setLength ( getInt(bucket, index +  4) );
        seq_handle.setOffset( getInt(bucket, index +  8) );
        seq_handle.setLength( getInt(bucket, index + 12) );
    }
    
    public void write(byte[] bucket, int slot) {
        int index = slot * 16;
        setInt(id_handle.getOffset(), bucket, index);
        setInt(id_handle.getLength(), bucket, index + 4);
        setInt(seq_handle.getOffset(), bucket, index + 8);
        setInt(seq_handle.getLength(), bucket, index + 12);
    }
    
    private int getInt(byte[] bucket, int index) {
        return bucket[index] << 24 | bucket[index+1] << 16 | bucket[index+2] << 8 | bucket[index+3];
        
    }
    
    private void setInt(int value, byte[] bucket, int index) {
        bucket[index] = (byte) (value >> 24);
        bucket[index + 1] = (byte) ((0x00ffffff & value) >> 16);
        bucket[index + 2] = (byte) ((0x0000ffff & value) >> 8);
        bucket[index + 3] = (byte) (0x000000ff & value);
    }
    
}
