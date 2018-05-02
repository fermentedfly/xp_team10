package at.tugraz.xp10.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ShoppingListItem {
    private String name;
    private Double quantity;
    private Double unitprice;
    private String category;
    private String tempId;
    // todo unit
    private Boolean IsPurchased;

    public ShoppingListItem(String name, Double quantity, Double unitprice, String category, Boolean checked, String tempId) {
        this.name = name;
        this.quantity = quantity;
        this.unitprice = unitprice;
        this.category = category;
        this.IsPurchased = checked;
        this.tempId = tempId;
    }

    public ShoppingListItem() {

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

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public Boolean getPurchased() {
        return IsPurchased;
    }

    public void setPurchased(Boolean purchased) {
        IsPurchased = purchased;
    }
}
