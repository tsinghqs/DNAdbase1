
import java.util.ArrayList;

public class FreeList {
    
    private ArrayList<FreeSection> list = new ArrayList<FreeSection>();
    
    public int getSize() {return list.size();}
    public ArrayList<FreeSection> getList() {return list;}
    
    public void add(int offset, int len) {
        
        FreeSection sec = new FreeSection(offset, len);
        
        int i = 0;
        for (; i < list.size(); i++) {
           if (sec.getOffset() < list.get(i).getOffset()) {
               break;
           }
        }
        // Add
        list.add(i, sec);
        
        // Merge with left node, if possible
        FreeSection left = i > 0 ? list.get(i - 1) : null;
        if (left != null && left.getOffset() + left.getLength() == sec.getOffset()) {
            left.setLength(left.getLength() + sec.getLength());
            list.remove(i);
            i--;
            sec = left;
        }
        
        // Merge with right node, if possible
        FreeSection right = i < list.size() - 1 ? list.get(i + 1) : null;
        if (right != null && sec.getOffset() + sec.getLength() == right.getOffset()) {
            sec.setLength(sec.getLength() + right.getLength());
            list.remove(i + 1);
        }
    }
    
    public int find(int len) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            FreeSection sec = list.get(i);
            if (len <= sec.getLength()) {
                index = i;
                break;
            }
        }
        // Either the list is empty, or there's no free section larger than len
        if (index == -1) {
            return -1;
        }
        
        FreeSection sec = list.get(index);
        sec.setLength(sec.getLength() - len);
        if (sec.getLength() == 0) {
            list.remove(index);
        }
        return sec.getOffset();
    }
}
