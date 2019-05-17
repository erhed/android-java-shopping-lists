package se.maj7.shoppinglisttwo;

import android.os.Parcelable;

import java.io.Serializable;

public class ShoppingListItem implements Serializable {
    private String mType;
    private int mAmount;
    private boolean mChecked;

    public ShoppingListItem(String type, int amount) {
        this.mType = type;
        this.mAmount = amount;
    }

    // GET

    public String getType() {
        return mType;
    }

    public int getAmount() {
        return mAmount;
    }

    public boolean isChecked() {
        return mChecked;
    }

    // SET

    public void checkItem() {
        mChecked = true;
    }

    public void uncheckItem() {
        mChecked = false;
    }
}
