package at.tugraz.xp10.firebase;

import java.util.ArrayList;

import at.tugraz.xp10.model.Category;

public interface CategoriesValueEventListener {
    void onNewData(ArrayList<Category> Categories);
}
