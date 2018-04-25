package at.tugraz.xp10.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {
    private String eMail;
    private String firstName;
    private String lastName;
    private HashMap<String, Boolean> shoppinglists;

    public User() {
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

    @Override
    public String toString() {
        return "User{" +
                "eMail='" + eMail + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shoppinglists=" + shoppinglists +
                '}';
    }
}
