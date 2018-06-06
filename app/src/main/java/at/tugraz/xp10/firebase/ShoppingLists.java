package at.tugraz.xp10.firebase;
import at.tugraz.xp10.model.ShoppingList;

public class ShoppingLists {

    private Database mDBRef;

    public ShoppingLists()
    {
        mDBRef =  Database.getInstance("shoppinglists");
    }

    public void getShoppingList(String key, final DatabaseValueEventListener listener)
    {
        mDBRef.installValueListener(ShoppingList.class, key, listener);
    }

    public void updateShoppingList(String key, ShoppingList list)
    {
        if(key == null)
        {
            mDBRef.pushValue(list);
        }
        else
        {
            mDBRef.setValue(key, list);
        }
    }
}
