package se.maj7.shoppinglisttwo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class EditListAdapter extends RecyclerView.Adapter<EditListAdapter.ListItemHolder> {

    private ArrayList<ShoppingListItem> mListItems;
    private EditListActivity mEditListActivity;

    public EditListAdapter(EditListActivity editListActivity, ArrayList<ShoppingListItem> items) {

        mEditListActivity = editListActivity;
        mListItems = items;
    }

    @NonNull
    @Override
    public EditListAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_list, parent, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemHolder holder, int position) {
        // List item
        ShoppingListItem item = mListItems.get(position);

        // Type
        holder.mType.setText(item.getType());

        // Amount
        holder.mAmount.setText(String.valueOf(item.getAmount()));

        // Set style for checked/unchecked
        if (item.isChecked()) {
            holder.mContainer.setBackgroundResource(R.color.itemBackgroundChecked);
            holder.mType.setTextColor(Color.parseColor("#bababa"));
            holder.mAmount.setTextColor(Color.parseColor("#bababa"));
        } else {
            holder.mContainer.setBackgroundResource(0);
            holder.mType.setTextColor(Color.parseColor("#000000"));
            holder.mAmount.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mType;
        TextView mAmount;
        LinearLayout mContainer;

        public ListItemHolder(View view) {
            super(view);

            mType = (TextView) view.findViewById(R.id.shoppingListItem);
            mAmount = (TextView) view.findViewById(R.id.shoppingListQty);
            mContainer = (LinearLayout) view.findViewById(R.id.shoppingListItemContainer);

            view.setClickable(true);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ShoppingListItem item = mListItems.get(getAdapterPosition());
            if (item.isChecked()) {
                item.uncheckItem();
            } else {
                item.checkItem();
            }
            mEditListActivity.updateList();
        }
    }

    public void deleteItem(int position) {
        mListItems.remove(position);
        notifyItemRemoved(position);
    }
}