package at.tugraz.xp10.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import at.tugraz.xp10.model.ShoppingListItem;

public class ShoppingListItems {

    private DatabaseReference mDBRef;

    public ShoppingListItems()
    {
        mDBRef = FirebaseDatabase.getInstance().getReference().child("items");
    }

    public void getShoppingListItems(String key, final ShoppingListItemsValueEventListener listener)
    {
        mDBRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, ShoppingListItem> data = new HashMap<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ShoppingListItem item = ds.getValue(ShoppingListItem.class);
                    data.put(ds.getKey(), item);
                }
                listener.onNewData(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addItemToShoppingList(String ListKey, String name, Double quantity, String unit, String category, Boolean checked)
    {
        String itemKey = mDBRef.child(ListKey).push().getKey();
        ShoppingListItem item = new ShoppingListItem(name, quantity, unit, category, checked, itemKey);
        mDBRef.child(ListKey).child(itemKey).setValue(item);
    }

    public void deleteItemFromShoppingList(String ListKey, String ItemKey)
    {
        mDBRef.child(ListKey).child(ItemKey).removeValue();
    }

    public void updateItemInShoppingList(String ListKey, String ItemKey, String name, Double quantity, String unit, String category, Boolean checked)
    {
        mDBRef.child(ListKey).child(ItemKey).setValue(new ShoppingListItem(name, quantity, unit, category, checked, ItemKey));
    }
}
