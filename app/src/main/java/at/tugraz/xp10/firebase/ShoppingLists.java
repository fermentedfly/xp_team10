package at.tugraz.xp10.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.tugraz.xp10.model.ShoppingList;

public class ShoppingLists {

    private DatabaseReference mDBRef;

    public ShoppingLists()
    {
        mDBRef = FirebaseDatabase.getInstance().getReference().child("shoppinglists");
    }

    public void getShoppingList(String key, final ShoppingListValueEventListener listener)
    {
        mDBRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onNewData(dataSnapshot.getValue(ShoppingList.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateShoppingList(String key, ShoppingList list)
    {
        if(key == null) {
            key = mDBRef.push().getKey();
        }

        mDBRef.child(key).setValue(list);
    }
}
