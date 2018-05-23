package at.tugraz.xp10.firebase;

import java.util.HashMap;

import at.tugraz.xp10.model.Category;

public interface CategoriesValueEventListener {
    void onNewData(HashMap<String, Category> Categories);
}
