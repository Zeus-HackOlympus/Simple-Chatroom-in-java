/**
 * @author      : HackOlympus (zeus@hackolympus)
 * @file        : UserList
 * @created     : Sunday Mar 12, 2023 05:05:11 MST
 */
import java.util.*; 
import java.io.*; 

public class UserList implements Serializable
{
    private ArrayList<User> list; 
    
    public UserList()
    {
        list = new ArrayList<User>();
    }
    
    public void addUser(String username, String password) 
    {
        if (this.exists(username) != null) {
            User u = new User(username, password);
            list.add(u);
            System.out.println("User registered successfully");
        } else {
            System.out.println(username + " exists. Try some other username");
        }
    }



    public User exists(String username) 
    {
        for (User u: list) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }

        return null;
    }

}


