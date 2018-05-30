package at.tugraz.xp10.firebase;

import java.util.HashMap;

public interface DatabaseListValueEventListener {
    <T> void onNewData(HashMap<String, T> data);
}
