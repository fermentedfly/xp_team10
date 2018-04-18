package at.tugraz.xp10.model;

import java.util.List;

public class ShoppingList {
    private String id;
    private String title;
    private String description;
    private List<String> userIds;
    private List<ShoppingListItem> items;

    public ShoppingList() {
    }

    public ShoppingList(String id, String title, String description, List<String> userIds, List<ShoppingListItem> items) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userIds = userIds;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
    }
}
