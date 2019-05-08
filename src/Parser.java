import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Parser executes each command input.
 * 
 * @author tsingh
 * @version 3.17.2019
 *
 */
public class Parser {

    /**
     * parseBoy executes each command.
     */
    private Scanner scan;
    private HashTable hashboy;
    private MemoryManager memboy;


    /**
     * Constructor for parser
     * 
     * @param fp
     *            name of file
     * @param hashing
     *            Name of hashtable
     * 
     * @param meming
     *            Name of memeoryManager
     * @throws FileNotFoundException
     *             if file not found
     */
    public Parser(File fp, HashTable hashing, MemoryManager meming)
        throws FileNotFoundException {
        scan = new Scanner(fp);
        hashboy = hashing;
        memboy = meming;
    }


    /**
     * Parses input for commands.
     * 
     * @throws IOException
     *             if there is IO
     */
    public void parseString() throws IOException {
        CommandExecution commando = new CommandExecution(hashboy, memboy);
        while (this.scan.hasNextLine()) {
            String line = this.scan.nextLine();
            String[] commands = line.trim().split("\\s+");
            if (commands[0].equals("insert")) {
                String seqId = commands[1];
                line = this.scan.nextLine();
                commando.insert(seqId, line);
            }
            else if (commands[0].equals("remove")) {
                commando.remove(commands[1]);
            }
            else if (commands[0].equals("search")) {
                commando.search(commands[1]);
            }
            else if (commands[0].equals("print")) {
                commando.print();
            }

        }
        scan.close();
        

    }
}
