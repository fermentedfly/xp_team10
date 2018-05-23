package at.tugraz.xp10.firebase;

import java.util.HashMap;

import at.tugraz.xp10.model.User;

public interface UsersValueEventListener {
    void onNewData(HashMap<String, User> data);
}
