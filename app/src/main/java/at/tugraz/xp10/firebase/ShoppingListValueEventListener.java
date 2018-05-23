package at.tugraz.xp10.firebase;

import java.util.HashMap;
import at.tugraz.xp10.model.ShoppingList;

public interface ShoppingListValueEventListener {
    void onNewData(ShoppingList data);
}
