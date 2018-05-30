package at.tugraz.xp10.firebase;

import at.tugraz.xp10.model.User;

public interface UserValueEventListener {
    void onNewData(User user);
}
