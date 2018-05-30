package at.tugraz.xp10.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import at.tugraz.xp10.util.Constants;

@IgnoreExtraProperties
public class ShoppingList {

    private String title;
    private String description;
    private Map<String, String> members;
    private HashMap<String, Boolean> items;
    private final Object createdAt = ServerValue.TIMESTAMP;

    public ShoppingList() {
    }

    public ShoppingList(String title, String description, String ownerId) {
        this.title = title;
        this.description = description;
        this.members = new HashMap<>();
        this.items = new HashMap<>();
        this.members.put(ownerId, Constants.OWNER);
    }


    public Map<String, String> getMembers() {
        return members;
    }

    public void setMembers(Map<String, String> members) {
        this.members = members;
    }

    public HashMap<String, Boolean> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Boolean> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getCreatedAt() {
        return createdAt;
    }
}
