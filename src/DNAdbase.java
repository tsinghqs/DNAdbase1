
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class DNAdbase {
    
    private static HashTable hash_table;
    private static MemoryManager mem_mgr;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        if (args.length != 4) {
            System.exit(1);
        }
        
        String cmd_file = args[0];
        String hash_file = args[1];
        int hash_table_size = Integer.parseInt(args[2]);
        String memory_file = args[3];
        
        hash_table = new HashTable(hash_file, hash_table_size);
        mem_mgr = new MemoryManager(memory_file);
        
        process_commands(cmd_file);
        
        hash_table.close();
        mem_mgr.close();
    }
    
    private static void process_commands(String cmd_file) throws FileNotFoundException, IOException {
        
        Scanner scanner = new Scanner(new File(cmd_file));
        while (scanner.hasNext()) {
            
            String cmd = scanner.next();
            if (cmd.equals("insert")) {
                String id = scanner.next();
                int len = scanner.nextInt();
                scanner.nextLine();
                String seq = scanner.nextLine();
                insert(id, seq);
            } else if (cmd.equals("remove")) {
                String id = scanner.next();
                remove(id, mem_mgr);
            } else if (cmd.equals("print")) {
                print();
            } else if (cmd.equals("search")) {
                String id = scanner.next();
                search(id);
            }
        }
        scanner.close();
    }
    
    private static void insert(String id, String seq) throws IOException {
        int slot_num = (int) SFold.hash(id, 64);
        if (hash_table.contains(slot_num, id, mem_mgr)) {
            hash_table.remove(slot_num, id, mem_mgr, false);
        }
        
        Handle id_handle = mem_mgr.insert(id);
        Handle seq_handle = mem_mgr.insert(seq);
        
        if (! hash_table.insert(slot_num, id_handle, seq_handle)) {
            System.out.println("Insert failed: Bucket is full");
            mem_mgr.remove(id_handle);
            mem_mgr.remove(seq_handle);
        }
    }
    
    private static void remove(String id, MemoryManager mem_mgr) throws IOException {
        int slot_num = (int) SFold.hash(id, 64);
        hash_table.remove(slot_num, id, mem_mgr, true);
    }
    
    private static void print() throws IOException {
        hash_table.print(mem_mgr);
        mem_mgr.print();
    }
    
    private static void search(String id) throws IOException {
        int slot_num = (int) SFold.hash(id, 64);
        hash_table.search(slot_num, id, mem_mgr);
    }
    
}
