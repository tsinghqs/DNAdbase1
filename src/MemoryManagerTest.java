import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * @author vpratha
 * @version 5.7.2019
 * 
 * Test class for MemoryManager.
 * 
 */
public class MemoryManagerTest extends TestCase 
{
    /**
     * field for testing
     */
    private MemoryManager mem;
    
    
    /**
     * setup
     * @throws FileNotFoundException 
     */
    public void setUp() throws FileNotFoundException
    {
        RandomAccessFile raf = new RandomAccessFile("hello.txt", "rw");
        mem = new MemoryManager(raf);
    }
    
    /**
     * Tests adjacent method.
     */
    public void testAdjacent()
    {
        Handle h1 = new Handle(1, 4);
        Handle h2 = new Handle(2, 10);
        Handle h3 = new Handle(3, 8);
        assertTrue(mem.isAdjacent(h1, h2));
        assertFalse(mem.isAdjacent(h1, h3));
        mem.printFreelist();
    }
    
    /**
     * Tests getBinaryRep method.
     */
    public void testGetBinaryRep()
    {
        String test1 = "TACG";
        byte[] test1B = mem.getBinaryRep(test1);
        assertEquals(test1B[0], -58);
        String test2 = "TAC";
        byte[] test2B = mem.getBinaryRep(test2);
        assertEquals(test2B.length, 1);
    }

}
