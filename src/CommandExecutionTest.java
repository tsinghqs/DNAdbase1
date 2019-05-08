import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * @author vpratha
 * @version 5.7.2019
 * 
 * Test class for CommandExecution
 * 
 */
public class CommandExecutionTest extends TestCase 
{
    /**
     * field for testing
     */
    private CommandExecution commando;
    
    
    /**
     * setup
     * @throws FileNotFoundException 
     */
    public void setUp() throws FileNotFoundException
    {
        RandomAccessFile raf = new RandomAccessFile("hello.txt", "rw");
        MemoryManager mem = new MemoryManager(raf);
        HashTable hash = new HashTable(64, mem);
        commando = new CommandExecution(hash, mem);
    }
    
    /**
     * Tests adjacent method.
     * @throws IOException io exception
     */
    public void testInsert() throws IOException
    {
        commando.insert("AAA", "AAAAA");
        commando.insert("AAA", "AAAAA");
        commando.search("AAA");
        commando.search("BBB");
        commando.remove("AAA");
        commando.remove("ACT");
        commando.print();
        int hashslot = (int)commando.sfold("AAA");
        assertNotNull(hashslot);
    }

}
