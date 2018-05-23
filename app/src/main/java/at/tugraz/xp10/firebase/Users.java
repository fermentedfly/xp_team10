package at.tugraz.xp10.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import at.tugraz.xp10.model.User;

public class Users {

        private DatabaseReference mDBRef;

        public Users() {
            mDBRef = FirebaseDatabase.getInstance().getReference().child("users");
        }

        public void getUsers(final UsersValueEventListener listener)
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

        public String getCurrentUserID()
        {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setUser(String key, User user)
        {
            mDBRef.child(key).setValue(user);
        }
}
