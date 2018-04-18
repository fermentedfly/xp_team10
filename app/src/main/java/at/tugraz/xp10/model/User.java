package at.tugraz.xp10.model;

public class User {
    private String eMail;
    private String firstName;
    private String lastName;

    public User() {
    }

    public User(String eMail, String firstName, String lastName) {
        this.eMail = eMail;
        this.firstName = firstName;
        this.lastName = lastName;
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
}
