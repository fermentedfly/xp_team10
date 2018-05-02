package at.tugraz.xp10.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class ShoppingListItem {
    private String shoppingListId;
    private String name;
    private Double quantity;
    // todo unit
    private Boolean checked;

    public ShoppingListItem(String shoppingListId, String name, Double quantity, Boolean checked) {
        this.shoppingListId = shoppingListId;
        this.name = name;
        this.quantity = quantity;
        this.checked = checked;
    }

    public ShoppingListItem() {

    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(String shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
