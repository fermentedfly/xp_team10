package at.tugraz.xp10.firebase;

import at.tugraz.xp10.model.ModelBase;

public interface DatabaseValueEventListener {
    <T extends ModelBase> void onNewData(T data);
}
