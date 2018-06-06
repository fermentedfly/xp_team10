package at.tugraz.xp10.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import at.tugraz.xp10.model.User;

import static android.content.ContentValues.TAG;

public class Users {

        private DatabaseReference mDBRef;

        public Users() {
            mDBRef = FirebaseDatabase.getInstance().getReference().child("users");
        }

        public void getUsers(final UserListValueEventListener listener)
        {
            mDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, User> data = new HashMap<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        data.put(ds.getKey(), user);
                    }
                    listener.onNewData(data);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void getUser(String key, final UserValueEventListener listener)
        {
            mDBRef.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    listener.onNewData(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void getCurrentUser(final UserValueEventListener listener)
        {
            getUser(getCurrentUserID(), listener);
        }

        public String getCurrentUserID()
        {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setUser(String key, User user)
        {
            mDBRef.child(key).setValue(user);
        }

        public void setCurrentUser(User user)
        {
            setUser(getCurrentUserID(), user);
        }

        public void addShoppingListToUser(String UserKey, String ShoppingListKey)
        {
            HashMap<String, Object> newList = new HashMap<>();
            newList.put(ShoppingListKey, true);
            mDBRef.child(UserKey).child("shoppinglists").updateChildren(newList);
        }

        public void deleteUser(String key)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Log.d(TAG, "User not null.");
            }


            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                            }
                        }
                    });
        }
}
