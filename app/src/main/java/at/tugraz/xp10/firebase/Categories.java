package at.tugraz.xp10.firebase;

import java.util.HashMap;

import at.tugraz.xp10.model.Category;

public class Categories {

    private Database mDBRef;

    public Categories()
    {
        mDBRef =  Database.getInstance("categories");
    }

    public void getCategories(final CategoriesValueEventListener listener)
    {
        mDBRef.getListOfValues(Category.class, new DatabaseListValueEventListener() {
            @Override
            public <Category> void onNewData(HashMap<String, Category> data) {
                listener.onNewData((HashMap<String, at.tugraz.xp10.model.Category>) data);
            }
        });

    }

    public void putCategory(Category c)
    {
        mDBRef.pushValue(c);
    }
}