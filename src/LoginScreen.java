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
                if (arg.length > 1) {
                    join(arg[1]);
                } else {
                    System.out.println("Please provide room name\n");
                }
                break;
            case 'c':
                if (arg.length > 1) {
                    Pattern pattern = Pattern.compile("^[a-z0-9]+$");
                    Matcher matcher = pattern.matcher(arg[1]); 
                    if (matcher.matches()) {
                        create(arg[1]);
                    }
                } else {
                    System.out.println("Please provide room name\n");
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
        if (result) { 
            Room room = new Room(db, roomName, this.username);
            room.enter();
        }
        return result; 
    }

    public void account() {
        System.out.println("Please select from the following options: ");
        System.out.println("1. Change Username\n2. Change Username\n3. Quit\n");
        
        int inp = in.nextInt();

        if (inp == 1) {
            in.next(); // for flushing input
            changeUsername();
        } else if (inp == 2) {
            in.next();
            changePassword();
        } else if (inp == 3) {
            in.next();
            menu(); // go back to menu
        } else {
            System.out.println("Please select a valid option");
            account();
        }
    }

    public void changeUsername() { 
        System.out.print("Enter new username: ");
        String newUsername = in.nextLine();
        Pattern pattern = Pattern.compile("^[a-z0-9]+$");
        Matcher matcher = pattern.matcher(newUsername); 
        if (matcher.matches()) {
            db.changeUsername(this.username, newUsername);
        } else {
            System.out.println("Enter valid username. A valid username only contains lowercase letter and numbers");
            changeUsername();
        }
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


