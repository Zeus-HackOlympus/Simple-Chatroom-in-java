/**
 * @author      : HackOlympus (zeus@hackolympus)
 * @file        : Main
 * @created     : Sunday Mar 12, 2023 03:20:54 MST
 */
import java.util.*;
import java.io.*;
        
public class Main
{
    private Scanner in = new Scanner(System.in);
    private Database db; 
    private String username;
    
    public Main(Database data) {
        this.db = data;
        this.username = null;
    }

    public void start() {
        while (true) {
            menu();
        }
    }

    private void menu() {
        System.out.println("Welcome to zeus's chat App!\n"); 
        System.out.println("Please select from the following options:");
        System.out.println("(R)egister, (L)ogin, (Q)uit");
        System.out.println("------------------------------------------");
        
        switch (Character.toLowerCase(in.nextLine().trim().charAt(0))) {
            case 'l':
                login();
                LoginScreen mainView = new LoginScreen(this.username, this.db);
                mainView.init();
                break;
            
            case 'r':
                register();
                break;
            case 'q': 
                System.out.println("Quitting...");
                System.exit(0);
            default:
                System.out.println("Provide correct option");
                break;
        }
    }

    private boolean register() {
        System.out.print("Username: ");
        String username = in.nextLine();
        Console console = System.console();
        char[] pass = console.readPassword("Password: ");
        String password = new String(pass);
        return db.registerUser(username, password);
    }

    private boolean login() {
        System.out.println("Username: ");
        String username = in.nextLine();
        Console console = System.console();
        char[] pass = console.readPassword("Password: ");
        String password = new String(pass);
        if (db.loginUser(username, password)) {
            System.out.println("Login successful");
            this.username = username;
            return true;
        } else {
            System.out.println("Incorrect username/password");
            this.username = null;
            return false;
        }
    }

    

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Console console = System.console();

        String url = "jdbc:postgresql://localhost:5432/mydb"; 
        
        System.out.print("Database username: ");
        String username = in.nextLine();

        char[] pass = console.readPassword("Database password: ");
        String password = new String(pass); 

        Database data = new Database(url, username, password);
        Main program = new Main(data);
        program.start();
        data.closeConnection();
    }
}


