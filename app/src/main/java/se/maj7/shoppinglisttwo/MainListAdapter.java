package se.maj7.shoppinglisttwo;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListItemHolder> {

    private ArrayList<ShoppingList> mMainList;
    private MainListActivity mMainActivity;

    public MainListAdapter(MainListActivity mainListActivity, ArrayList<ShoppingList> mainList) {

        mMainActivity = mainListActivity;
        mMainList = mainList;
    }

    @NonNull
    @Override
    public MainListAdapter.MainListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        return new MainListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListItemHolder holder, int position) {
        // List
        ShoppingList list = mMainList.get(position);

        // Date Created
        holder.mDateCreated.setText(list.getDateString());

        // Row 1
        holder.mListRow1.setText(list.getItem(0).getType());
        // Style for checked/unchecked
        if (list.getItem(0).isChecked()) {
            //holder.mListRow1.setPaintFlags(holder.mListRow1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mListRow1.setTextColor(Color.parseColor("#ACACAC"));
        }

        // Row 2
        if (list.getSize() > 1) {
            holder.mListRow2.setText(list.getItem(1).getType());
            // Style for checked/unchecked
            if (list.getItem(1).isChecked()) {
                //holder.mListRow2.setPaintFlags(holder.mListRow2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mListRow2.setTextColor(Color.parseColor("#ACACAC"));
            }
        } else {
            holder.mListRow2.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return mMainList.size();
    }

    public class MainListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mDateCreated;
        TextView mListRow1;
        TextView mListRow2;
        LinearLayout mContainer;

        public MainListItemHolder(View view) {
            super(view);

            // Layout textViews for Main list
            mDateCreated = (TextView) view.findViewById(R.id.mainDateCreated);
            mListRow1 = (TextView) view.findViewById(R.id.mainRow1);
            mListRow2 = (TextView) view.findViewById(R.id.mainRow2);
            mContainer = (LinearLayout) view.findViewById(R.id.mainListItemContainer);

            view.setClickable(true);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mMainActivity.showList(mMainList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public void deleteItem(int position) {
        mMainList.remove(position);
        notifyItemRemoved(position);
        mMainActivity.saveShoppingLists();
    }

}
