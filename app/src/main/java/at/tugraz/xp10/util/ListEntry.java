package at.tugraz.xp10.util;

import java.util.Objects;

import at.tugraz.xp10.model.ShoppingList;

public class ListEntry {
    public String id;
    public ShoppingList shoppingList;

    public ListEntry(String id, ShoppingList shoppingList) {
        this.id = id;
        this.shoppingList = shoppingList;
    }

}
