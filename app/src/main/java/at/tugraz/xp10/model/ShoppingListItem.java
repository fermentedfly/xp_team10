package at.tugraz.xp10.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ShoppingListItem {
    private String shoppingListId;
    private String name;
    private Double quantity;
    private Double unitprice;
    private String category;
    // todo unit
    private Boolean IsPurchased;

    public ShoppingListItem(String shoppingListId, String name, Double quantity, Double unitprice, String category, Boolean checked) {
        this.shoppingListId = shoppingListId;
        this.name = name;
        this.quantity = quantity;
        this.unitprice = unitprice;
        this.category = category;
        this.IsPurchased = checked;
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

    public Boolean getIsPurchased() {
        return IsPurchased;
    }

    public void setIsPurchased(Boolean checked) {
        this.IsPurchased = checked;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
