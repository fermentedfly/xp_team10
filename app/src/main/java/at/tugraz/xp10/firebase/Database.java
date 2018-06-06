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
    private HashMap<DatabaseEventListener, ValueEventListener> mListeners;

    private Database(String key)
    {
        mDBRef = FirebaseDatabase.getInstance().getReference().child(key);
        mListeners = new HashMap<>();
    }

    public static Database getInstance(String key)
    {
        return new Database(key);
    }

    public <T extends ModelBase> void installListListener(final Class<T> typeParameterClass,
                                                          final DatabaseListValueEventListener listener)
    {
        ValueEventListener inner_listener = new ValueEventListener() {
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
        };
        mListeners.put(listener, inner_listener);
        mDBRef.addValueEventListener(inner_listener);
    }

    public <T extends ModelBase> void installValueListener(final Class<T> typeParameterClass, String key, final DatabaseValueEventListener listener)
    {
        ValueEventListener inner_listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T data = dataSnapshot.getValue(typeParameterClass);
                listener.onNewData(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mListeners.put(listener, inner_listener);
        mDBRef.child(key).addValueEventListener(inner_listener);
    }

    public <T> void pushValue(T value)
    {
        mDBRef.push().setValue(value);
    }

    public <T> void setValue(String key, T value)
    {
        mDBRef.child(key).setValue(value);
    }

    public String getNewKey() {
        return mDBRef.push().getKey();
    }

    public void deleteValue(String key)
    {
        mDBRef.child(key).removeValue();
    }

    public void uninstallAllListeners()
    {
        for(ValueEventListener listener : mListeners.values())
        {
            mDBRef.removeEventListener(listener);
        }
        mListeners.clear();
    }

    public HashMap<DatabaseEventListener, ValueEventListener> getListeners()
    {
        return mListeners;
    }
}
