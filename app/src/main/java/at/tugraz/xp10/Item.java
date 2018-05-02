package at.tugraz.xp10;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Item {
    public boolean isPurchased;
    public String name;
    public String category;
    public Long price;
    public Long quantity;

    public Item () {}

    public Item (String name_, String category_, Long price_, Long quantity_)
    {
        isPurchased = false;
        name = name_;
        category = category_;
        price = price_;
        quantity = quantity_;
    }
}
