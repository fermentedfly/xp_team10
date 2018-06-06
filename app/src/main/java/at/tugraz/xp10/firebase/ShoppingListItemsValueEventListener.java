package at.tugraz.xp10.firebase;

import java.util.HashMap;

import at.tugraz.xp10.model.ShoppingListItem;

public interface ShoppingListItemsValueEventListener {
    void onNewData(HashMap<String, ShoppingListItem> Items);
}
