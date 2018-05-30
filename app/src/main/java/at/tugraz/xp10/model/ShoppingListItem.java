package at.tugraz.xp10.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ShoppingListItem {
    private String name;
    private Double quantity;
    private String category;
    private String tempId;
    private String unit;
    private Boolean IsPurchased;

    public ShoppingListItem(String name, Double quantity, String unit, String category, Boolean checked, String tempId) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.IsPurchased = checked;
        this.tempId = tempId;
        this.unit = unit;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
