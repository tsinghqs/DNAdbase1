import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * This is class responsible for running the project properly. 
 * @author tsinghqs
 * @version 2018.02.19
 *
 */
public class DNAdbase 
{
    /**
     * This is the main class and function that runs the project
     * 
     * @param args a string value to run the project
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
        //read file as an argument
        String fileName = args[0];
        File file = new File(fileName);   
        RandomAccessFile memFile = new RandomAccessFile(args[3], "rw");
        MemoryManager mem = new MemoryManager(memFile);
        HashTable hash = new HashTable(Integer.parseInt(args[2]), mem);

        Parser parse = new Parser(file, hash, mem);
        parse.parseString();
        memFile.close();
        
       
    }
}