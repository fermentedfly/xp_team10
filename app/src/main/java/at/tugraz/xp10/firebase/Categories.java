package at.tugraz.xp10.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import at.tugraz.xp10.model.Category;

public class Categories {

    private DatabaseReference mCategories;

    public Categories()
    {
        mCategories = FirebaseDatabase.getInstance().getReference().child("categories");
    }

    public void getCategories(final CategoriesValueEventListener listener)
    {
        mCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Category> data = new ArrayList<>();
                Set<String> keys = ((HashMap<String, Object>)dataSnapshot.getValue()).keySet();
                for (String key : keys)
                {
                    Category item = dataSnapshot.child(key).getValue(Category.class);
                    data.add(item);
                }

                listener.onNewData(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void put(Category c)
    {
        mCategories.push().setValue(c);
    }
}