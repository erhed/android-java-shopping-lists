package se.maj7.shoppinglisttwo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerView.Adapter mAdapter;

    private Drawable icon;
    private final ColorDrawable background;

    public SwipeToDeleteCallback(RecyclerView.Adapter adapter, AppCompatActivity mActivity) {
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
        this.mAdapter = mAdapter;

        icon = ContextCompat.getDrawable(mActivity, R.drawable.ic_clear_white_24dp);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        // Delete shopping list (MainList) or list item (EditList)

        if (mAdapter instanceof MainListAdapter) {
            MainListAdapter mainListAdapter = (MainListAdapter) mAdapter;
            mainListAdapter.deleteItem(position);
        } else if (mAdapter instanceof EditListAdapter) {
            EditListAdapter editListAdapter = (EditListAdapter) mAdapter;
            editListAdapter.deleteItem(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0, 0 ,0,0);
        }

        background.draw(c);
        icon.draw(c);
    }


}