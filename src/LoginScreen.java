/**
 * @author      : HackOlympus (zeus@hackolympus)
 * @file        : LoginScreen
 * @created     : Tuesday Mar 14, 2023 18:43:14 MST
 */
import java.util.*;
import java.io.Console;
import java.util.regex.*;

public class LoginScreen
{
    private Scanner in = new Scanner(System.in);
    private Database db;
    private String username;
    private boolean loggedIn = false;

    public LoginScreen(String user, Database database) {
        this.username = user;
        this.db = database;
        this.loggedIn = true;
    }

    public void menu() {
        System.out.println("Please select from the following options: ");
        System.out.println("(J)oin, (C)reate, (A)ccount, (L)ogout");
        System.out.println("-------------------------------------------");

        String[] arg = in.nextLine().split(" ", 2);

        switch (Character.toLowerCase(arg[0].charAt(0))) {
            case 'j':
                join(arg[1]);
                break;
            case 'c':
                Pattern pattern = Pattern.compile("^[a-z0-9]+$");
                Matcher matcher = pattern.matcher(arg[1]); 
                if (matcher.matches()) {
                    create(arg[1]);
                }
                break;
            case 'a':
                account(); 
                break; 
            case 'l':
                logout();
                break;
            default:
                System.out.println("Please enter a valid option");
                menu();
                break;
        }
    }

    public boolean create(String roomName) {
       db.createRoom(roomName);  
       return join(roomName);
    }

    public boolean join(String roomName) {
        boolean result = db.joinRoom(roomName, this.username);  
        Room room = new Room(roomName);
        room.init();
        return result;
    }

    public void account() {
        System.out.println("Please select from the following options: ");
        System.out.println("1. Change Username\n2. Change Username\n3. Quit\n");
        
        int inp = in.nextInt();

        if (inp == 1) {
            changeUsername();
        } else if (inp == 2) {
            changePassword();
        } else if (inp == 3) {
            menu(); // go back to menu
        } else {
            System.out.println("Please select a valid option");
            account();
        }
    }

    public boolean changeUsername() { 
        System.out.print("Enter new username: ");
        String newUsername = in.nextLine();
        return db.changeUsername(this.username, newUsername);
    }

    public boolean changePassword() {
        System.out.print("Please enter your old password: ");

        Console console = System.console();
        char[] oldPass = console.readPassword("Old password: ");
        String oldPassword = new String(oldPass);

        if (db.checkPassword(this.username, oldPassword)) {
            char[] newPass = console.readPassword("New password: ");
            String newPassword = new String(newPass);
            db.changePassword(this.username, newPassword);
        } else {
            System.out.println("Old password is incorrect\n");
            changePassword();
        }
        return false;
    }

    public void logout() {
        this.loggedIn = false;
    }

    public void init() {
        while (loggedIn) {
            menu();
        }
    }
}


