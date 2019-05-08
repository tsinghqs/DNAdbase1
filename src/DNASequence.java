
public class DNASequence {
    
    public static byte[] encode(String s) {
        
        int length = s.length();
        int nbytes = (length - 1) / 4 + 1;
        if (nbytes == 0) {
            return null;
        }
        
        byte[] ret = new byte[nbytes];
        for (int i = 0; i < nbytes; i++) {
            int start_index = i * 4;
            int end_index = Math.min(start_index + 4, length);
            ret[i] = toByte(s, start_index, end_index);
        }
        return ret;
    }
    
    public static String decode(byte[] bytes, int len) {
        StringBuilder sb = new StringBuilder(len);
        int index = 0;
        for (int i = 0; i < len; i += 4) {
            append(sb, bytes[index], Math.min(4, len - i));
            index++;
        }
        return sb.toString();
    }
    
    private static byte toByte(String s, int start, int end) {
        int len = end - start;
        int ret = 0;
        for (int i = 0; i < len; i++) {
            ret = ret | ( getCode(s.charAt(start + i)) << (4 - i - 1)*2 );
        }
        return (byte) ret;
    }
    
    private static byte getCode(char c) {
        switch (c) {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
            default: return -1;
        }
    }
    
    private static char getChar(int c) {
        switch (c) {
            case 0: return 'A';
            case 1: return 'C';
            case 2: return 'G';
            case 3: return 'T';
            default: return '0';
        }
    }
    
    private static void append(StringBuilder sb, byte b, int len) {
        
        for (int i = 0; i < len; i++) {
            int shift = (4 - i - 1) * 2;
            int mask = 3 << shift;
            char ch = getChar ( (b & mask ) >> shift );
            sb.append(ch);
        }
    }
}
