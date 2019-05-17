package se.maj7.shoppinglisttwo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingList implements Serializable {

    private ArrayList<ShoppingListItem> mList;
    private String mDateCreatedString;
    private Date mDateCreated;

    public ShoppingList() {
        this.mList = new ArrayList<>();

        // Set date

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.mDateCreatedString = dateFormat.format(new Date());
        this.mDateCreated = new Date();
    }

    // Add item

    public void addItem(ShoppingListItem item) {
        mList.add(item);
    }

    // Clear list

    public void removeAllItems() {
        mList.clear();
    }

    // GET

    public ShoppingListItem getItem(int item) {
        return mList.get(item);
    }

    public String getDateString() {
        return mDateCreatedString;
    }

    public Date getDate() {
        return mDateCreated;
    }

    public int getSize() {
        return mList.size();
    }
}
