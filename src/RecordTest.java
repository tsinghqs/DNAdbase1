import student.TestCase;
/**
 * Class to test Record class
 * @author tsingh
 * @version 2019
 */
public class RecordTest extends TestCase {
    
    /**
     * Field for testing.
     */
    private Record tester;
    private Handle han;
    private Handle han2;
    
    /**
     *  Sets up test cases.
     */
    public void setUp() 
    {
        han = new Handle(1, 2);
        han2 = new Handle(3, 4);
        tester = new Record(han, han2);
    }
    
    /**
     * Test the handle methods
     */
    public void testMethods() {
        Handle check = tester.getSeqHandle();
        Handle seqid = tester.getSeqIDHandle();
        tester.setSeqHandle(han);
        tester.setSeqIDHandle(han2);
        check = tester.getSeqHandle();
        seqid = tester.getSeqIDHandle();
        assertEquals(seqid.getOffset(), tester.getSeqIDHandle().getOffset());
        assertEquals(check.getOffset(), tester.getSeqHandle().getOffset());

    }

   
   
    
}