/**
 * @author      : HackOlympus (zeus@hackolympus)
 * @file        : User
 * @created     : Sunday Mar 12, 2023 03:06:01 MST
 */

import java.util.*;

public class User
{
    private String username; 
    private String password;
    private boolean loggedIn;
    
    Scanner in = new Scanner(System.in);

    public User(String username, String password) {
        this.username = username; 
        this.password = password;
        loggedIn = false;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean changePassword(String oldPassword) {
        if (oldPassword.equals(password)) {
            String newPassword = in.nextLine();
            this.password = newPassword;
            System.out.println("Password change successful");
            return true;
        } else {
            System.out.println("Old password incorrect");
            return false;
        }
    }

    public boolean checkPassword(String pass) {
        if (this.password.equals(pass)) {
            return true;
        } else { 
            return false;
        }
    }

    public void loginSuccessful() {
        loggedIn = true;
    } 

    public void logout() {
        loggedIn = false;
    }
}


