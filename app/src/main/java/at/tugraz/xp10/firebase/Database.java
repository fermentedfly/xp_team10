package at.tugraz.xp10.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import at.tugraz.xp10.model.ModelBase;

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

    public <T extends ModelBase> void getListOfValues(final Class<T> typeParameterClass,
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

    public <T extends ModelBase> void getValue(final Class<T> typeParameterClass, String key, final DatabaseValueEventListener listener)
    {
        mDBRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T data = dataSnapshot.getValue(typeParameterClass);
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

    public <T> void setValue(String key, T value)
    {
        mDBRef.child(key).setValue(value);
    }
}
