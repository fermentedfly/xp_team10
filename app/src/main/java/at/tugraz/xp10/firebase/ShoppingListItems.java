package at.tugraz.xp10.firebase;

import at.tugraz.xp10.model.ShoppingListItem;

public class ShoppingListItems {

    private Database mDBRef;

    public ShoppingListItems()
    {
    }

    public void selectShoppingList(String key)
    {
        mDBRef = Database.getInstance("items/" + key);
    }

    public void getShoppingListItems(final DatabaseListValueEventListener listener)
    {
        mDBRef.installListListener(ShoppingListItem.class, listener);
    }

    public void addItemToShoppingList(String name, Double quantity, String unit, String category, Boolean checked)
    {
        String itemKey = mDBRef.getNewKey();
        ShoppingListItem item = new ShoppingListItem(name, quantity, unit, category, checked, itemKey);
        mDBRef.setValue(itemKey, item);
    }

    public void deleteItemFromShoppingList(String ItemKey)
    {
        mDBRef.deleteValue(ItemKey);
    }

    public void updateItemInShoppingList(String ItemKey, String name, Double quantity, String unit, String category, Boolean checked)
    {
        mDBRef.setValue(ItemKey, new ShoppingListItem(name, quantity, unit, category, checked, ItemKey));
    }
}
