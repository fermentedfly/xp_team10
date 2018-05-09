package at.tugraz.xp10.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {
    private String eMail;
    private String firstName;
    private String lastName;
    private HashMap<String, Boolean> shoppinglists;
    private HashMap<String, Boolean> friends;

    private UserListener userListener = null;

    public User() {
        this.shoppinglists = new HashMap<>();
    }

    public User(String eMail, String firstName, String lastName) {
        this.eMail = eMail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shoppinglists = new HashMap<>();
    }


    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public HashMap<String, Boolean> getShoppinglists() {
        return shoppinglists;
    }

    public void setShoppinglists(HashMap<String, Boolean> shoppinglists) {
        this.shoppinglists = shoppinglists;
    }

    public HashMap<String, Boolean> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, Boolean> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "eMail='" + eMail + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shoppinglists=" + shoppinglists +
                '}';
    }

    @Exclude
    public void setData(User data) {
        this.eMail = data.eMail;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.shoppinglists = data.shoppinglists;

        if (userListener != null) {
            userListener.onLoaded(this);
        }
    }

    public interface UserListener {
        public void onLoaded(User user);
    }

    @Exclude
    public void setUserListener(UserListener listener) {
        this.userListener = listener;
    }

    public void addFriend(String uid)
    {
        friends.put(uid, true);
    }

    public void removeFriend(String uid)
    {
        if(friends.containsKey(uid))
        {
            friends.remove(uid);
        }
    }

}
