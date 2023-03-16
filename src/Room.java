/**
 * @author      : HackOlympus (zeus@hackolympus)
 * @file        : Room
 * @created     : Wednesday Mar 15, 2023 21:28:05 MST
 */
import java.util.*;

public class Room
{
    private String roomName;
    private String username;
    private Database db; 
    private boolean presentInRoom;
    
    private Scanner in = new Scanner(System.in); 

    public Room(Database database, String roomName, String username)
    {
        this.roomName = roomName;
        this.username = username;
        this.db = database; 
        this.presentInRoom = true;
    }
    
    public void parseInput(String input) {
        if (input.charAt(0) == '/') {
            // if it's a command 
            handleCommand(input);
        } else {
            // if it's a normal message
            sendMessage(input);
        }
    }

    public void sendMessage(String msg) {
        this.db.sendMessage(this.roomName, this.username, msg);
    }

    public void handleCommand(String command) {
        if (command.trim().equals("/list")) {
            list();
        } else if (command.trim().equals("/leave")) {
            leave();
        } else if (command.trim().equals("/history")) {
            history();
        } else if (command.trim().equals("/help")) {
            help();
        } else {
            System.out.println("Not a valid command\n");
        }
    }

    public void list() {
        ArrayList<String> userlist = db.getUserList(this.roomName);
        System.out.println("Userlist: ");
        for (String username: userlist) {
            System.out.println(username);
        }
    } 

    public void leave() {
        if (db.leaveChatRoom(this.roomName, this.username)) {
            presentInRoom = false;
        } 
    } 

    public void history() {
        ArrayList<String> msgHistory = db.getChatHistory(this.roomName);
        System.out.println();
        for (String msg: msgHistory) {
            System.out.println(msg);
        }
        System.out.println();
    }

    public void help() {
        System.out.println("Supported commands: ");
        System.out.println("\t/list - Return a list of users currently in this chat room.");
        System.out.println("\t/leave - Exits the chat room.");
        System.out.println("\t/history - Print all past messages for the room");
        System.out.println("\t/help - Prints this menu");
    }

    public void enter() {
        while (presentInRoom) {
            System.out.printf("Welcome to \"%s\", \"%s\"\n", this.roomName, this.username);
            System.out.printf("Type \"/help\" for help\n");
            System.out.println("----------------------------------------------------------");
            String input = in.nextLine();
            parseInput(input);
        }
    }
}


