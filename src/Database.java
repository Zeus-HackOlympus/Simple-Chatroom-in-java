/**
 * @author      : HackOlympus (zeus@hackolympus)
 * @file        : Database
 * @created     : Monday Mar 13, 2023 22:35:32 MST

*/

import java.io.Console;
import java.util.*; 
import java.sql.*;

public class Database {
    
    private String url;
    private String username;
    private String password;
    
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public Database() {

        loginDatabase();

        try {
            stmt = conn.createStatement() ;
            // users table - to contain username/password combination
            String sql = "CREATE TABLE IF NOT EXISTS users" + 
                         "(id SERIAL PRIMARY KEY," + 
                         " username VARCHAR(60) NOT NULL UNIQUE," + 
                         " password VARCHAR(60) NOT NULL)";

            stmt.executeUpdate(sql);
            System.out.println("Users table initiated"); 

            // rooms table - to contain room name
            sql = "CREATE TABLE IF NOT EXISTS rooms" + 
                  "(id SERIAL PRIMARY KEY," + 
                  " name VARCHAR(60) NOT NULL UNIQUE)"; 
            stmt.executeUpdate(sql);
            System.out.println("rooms table initiated"); 

            // room_users table - to contain users present in room 
            sql = "CREATE TABLE IF NOT EXISTS room_users" + 
                  "(room_id VARCHAR(60) NOT NULL," +
                  " user_id VARCHAR(60) NOT NULL," + 
                  " PRIMARY KEY (room_id, user_id)," + 
                  " FOREIGN KEY (room_id) REFERENCES rooms(name)," + 
                  " FOREIGN KEY (user_id) REFERENCES users(username))";
            stmt.executeUpdate(sql);
            System.out.println("room_users table initiated"); 
            
            // Create messages table - to contain new msgs
            sql = "CREATE TABLE IF NOT EXISTS messages" + 
                  "(id SERIAL PRIMARY KEY," + 
                  " room_id VARCHAR(60) NOT NULL," +
                  " user_id VARCHAR(60) NOT NULL," + 
                  " message VARCHAR(255) NOT NULL," +
                  " created_at TIMESTAMP DEFAULT NOW()," + 
                  " FOREIGN KEY (room_id) REFERENCES rooms(name)," + 
                  " FOREIGN KEY (user_id) REFERENCES users(username))";
            stmt.executeUpdate(sql);
            System.out.println("messages table initiated"); 

        } catch (SQLException e) {
            e.printStackTrace();
        }

    } 

    public void loginDatabase() {
        try {
            Scanner in = new Scanner(System.in);
            Console console = System.console();
            this.url = "jdbc:postgresql://localhost:5432/mydb"; 
            
            System.out.print("Database username: ");
            this.username = in.nextLine();
    
            char[] pass = console.readPassword("Database password: ");
            this.password = new String(pass); 
            
            this.conn = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            loginDatabase();
        }
    }

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.executeUpdate();
            System.out.println("User registered successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
        
        return false;
    }

    public boolean checkPassword(String username,String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        boolean result = false;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("password");
                result = pass.equals(password);
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
        return result;
    }


    public boolean loginUser(String username, String password) {
        return checkPassword(username, password);
    }

    public boolean changeUsername(String oldUsername, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE username = ?";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername); 
            pstmt.executeUpdate();
            System.out.println("Username updated successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating username: " + e.getMessage());
        } 

        return false;
    }


    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
    
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username); 
            pstmt.executeUpdate();
            System.out.println("Password updated successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }

        return false;
    }

    public boolean createRoom(String roomName) {
        String sql = "INSERT INTO rooms (name) VALUES (?)";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName); 
            pstmt.executeUpdate();
            System.out.println("Room created successfully");
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating room: " + e.getMessage());
        }
        
        return false;
    }

    public boolean joinRoom(String roomName, String username) {
        String checkRoomSql = "SELECT id FROM rooms WHERE name=?";
        String sql = "INSERT INTO room_users (room_id, user_id) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(checkRoomSql);
            pstmt.setString(1, roomName);
            ResultSet rs = pstmt.executeQuery();

            // check if room exists or not 
            if (!rs.next()) {
                // if not then create room
                if (!createRoom(roomName)) {
                    System.out.println("Error creating room");
                    return false;
                }
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            pstmt.setString(2, username); 
            pstmt.executeUpdate();
            System.out.println("User joined room successfully");
            return true; 
        } catch (SQLException e) {
            System.out.println("Error joining room: " + e.getMessage());
        }
        
        return false;
    }
    
    public void sendMessage(String roomName, String username,String message) {
        String sql = "INSERT INTO messages (room_id, user_id, message) VALUES (?, ?, ?)";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            pstmt.setString(2, username);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }

    }

    public ArrayList<String> getChatHistory(String roomName) {
        ArrayList<String> chatHistory = new ArrayList<>();
        String sql = "SELECT user_id, message FROM messages WHERE room_id = ? ORDER BY id DESC LIMIT 20";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String message = rs.getString("user_id") + ": " + rs.getString("message");
                chatHistory.add(message);
            }
        } catch (SQLException e) {
            System.out.println("Error getting chat history: " + e.getMessage());
        }
        
        return chatHistory;
    }

    public ArrayList<String> getUserList(String roomName) {
        ArrayList<String> userList = new ArrayList<>();
        String sql = "SELECT user_id from room_users WHERE room_id = ? ORDER BY user_id";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName); 
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("user_id");
                userList.add(username);
            }
        } catch (SQLException e) {
            System.out.println("Error getting list of users: " + e.getMessage());
        }

        return userList;
    }
    
    public boolean leaveChatRoom(String roomName, String username) {
        String sql = "DELETE FROM room_users WHERE room_id=? AND user_id=?"; 
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomName);
            pstmt.setString(2, username);
            int numRowsDeleted = pstmt.executeUpdate();

            if (numRowsDeleted > 0) {
                System.out.println("User left room successfully");
                return true;
            } else {
                System.out.println("User was not found in the room");
            }
        } catch (SQLException e) {
            System.out.println("Error leaving room: " + e.getMessage());
        }
        
        return false;
    }

    public void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (conn != null) conn.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
