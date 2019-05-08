
public class Handle {
    private int offset;
    private int length;
    
    public Handle() {
        offset = 0;
        length = 0;
    }

    public Handle(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }
    
    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
    
    public boolean isEmpty() {
        return length == 0;
    }
    
    public boolean isFull() {
        return length != 0;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
