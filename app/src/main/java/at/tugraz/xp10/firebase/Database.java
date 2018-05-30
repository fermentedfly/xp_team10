package at.tugraz.xp10.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Database {

    private DatabaseReference mDBRef;

    private Database(String key)
    {
        mDBRef = FirebaseDatabase.getInstance().getReference().child(key);
    }

    public static Database getInstance(String key)
    {
        return new Database(key);
    }

    public <T> void getListOfValues(final Class<T> typeParameterClass,
                                    final DatabaseListValueEventListener listener)
    {
        mDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, T> data = new HashMap<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    T d = ds.getValue(typeParameterClass);
                    data.put(ds.getKey(), d);
                }
                listener.onNewData(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public <T> void pushValue(T value)
    {
        mDBRef.push().setValue(value);
    }
}
