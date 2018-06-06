package at.tugraz.xp10.firebase;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import at.tugraz.xp10.model.User;

public class Users {

    private Database mDBRef;

    public Users() {
        mDBRef =  Database.getInstance("users");
    }

    public void getUsers(final DatabaseListValueEventListener listener)
    {
        mDBRef.installListListener(User.class, listener);
    }

    public void getUser(String key, final DatabaseValueEventListener listener)
    {
        mDBRef.installValueListener(User.class, key, listener);
    }

    public void getCurrentUser(final DatabaseValueEventListener listener)
    {
        getUser(getCurrentUserID(), listener);
    }

    public String getCurrentUserID()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setUser(String key, User user)
    {
        mDBRef.setValue(key, user);
    }

    public void setCurrentUser(User user)
    {
        setUser(getCurrentUserID(), user);
    }

    public void addShoppingListToUser(String UserKey, String ShoppingListKey)
    {
        HashMap<String, Object> newList = new HashMap<>();
        newList.put(ShoppingListKey, true);
        mDBRef.updateChildren(UserKey + "/shoppinglists/", newList);
    }

    public void uninstallAllListeners()
    {
        mDBRef.uninstallAllListeners();
    }
}
