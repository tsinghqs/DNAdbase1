import student.TestCase;
/**
 * Class to test Handle class
 * @author tsingh
 * @version 2019
 */
public class HandleTest extends TestCase {
    
    /**
     * Field for testing.
     */
    private Handle tester;
    
    /**
     *  Sets up test cases.
     */
    public void setUp() 
    {
        tester = new Handle(1, 2);
    }
    
    /**
     * Test the handle methods
     */
    public void testMethods()
    {
        int check = tester.getOffset();
        assertEquals(check, 1);
        int len = tester.getLength();
        assertEquals(len, 2);
        tester.setOffset(3);
        tester.setLength(4);
        check = tester.getOffset();
        assertEquals(check, 3);
        len = tester.getLength();
        assertEquals(len, 4);
        
        
    }

   
   
    
}