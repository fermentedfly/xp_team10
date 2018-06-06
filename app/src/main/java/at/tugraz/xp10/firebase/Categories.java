package at.tugraz.xp10.firebase;

import at.tugraz.xp10.model.Category;

public class Categories {

    private Database mDBRef;

    public Categories()
    {
        mDBRef =  Database.getInstance("categories");
    }

    public void getCategories(final DatabaseListValueEventListener listener)
    {
        mDBRef.installListListener(Category.class, listener);
    }

    public void putCategory(Category c)
    {
        mDBRef.pushValue(c);
    }

}

