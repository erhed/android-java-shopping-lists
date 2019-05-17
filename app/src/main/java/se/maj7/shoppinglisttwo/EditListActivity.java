package se.maj7.shoppinglisttwo;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import java.util.ArrayList;

public class EditListActivity extends AppCompatActivity {

    private boolean isNewList;
    private ShoppingList mShoppingList;
    private ArrayList<ShoppingListItem> mListItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditListAdapter mAdapter;
    private int editListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        // New list or edit list

        Bundle extras = getIntent().getExtras();
        isNewList = extras.getBoolean("isNewList");

        if (isNewList) {
            // Create list
            mShoppingList = new ShoppingList();

            // Request focus on addItem text view and show keyboard
            TextView addItem = (TextView) findViewById(R.id.addItem);
            addItem.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            // Get list from MainListActivity
            Intent intent = getIntent();
            mShoppingList = (ShoppingList) intent.getSerializableExtra("shoppingList");
            for (int i = 0; i< mShoppingList.getSize(); i++) {
                mListItems.add(mShoppingList.getItem(i));
            }
            editListPosition = extras.getInt("position");
        }

        // Actionbar

        setupActionBar();
        TextView uncheckAll = (TextView) findViewById(R.id.uncheckAll);
        uncheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckAllItems();
            }
        });

        // Handle 'Next' and 'Done' keys on keyboard when adding item

        handleDoneButton();

        // RecyclerView

        setupRecyclerView();

    }

    // Setup view

    private void setupActionBar() {

        // Action bar, back button, title text, text size and background color

        if (getSupportActionBar() != null) {
            // Back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimaryDark)));

            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.action_bar_title, null);

            // Set action bar title text and size
            ((TextView) v.findViewById(R.id.title)).setText(mShoppingList.getDateString());
            ((TextView) v.findViewById(R.id.title)).setTextSize(16);

            // "UNCHECK ALL"-button
            ((TextView) v.findViewById(R.id.uncheckAll)).setText("UNCHECK ALL");
            ((TextView) v.findViewById(R.id.uncheckAll)).setTextSize(16);

            this.getSupportActionBar().setCustomView(v);
        }
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        mAdapter = new EditListAdapter(this, mListItems);
        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(eLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set the adapter
        recyclerView.setAdapter(mAdapter);

        // Swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Action bar back-button action

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            prepareSendShoppingList();
            Intent intent = new Intent(this, MainListActivity.class);
            intent.putExtra("shoppingList", mShoppingList);
            if (mShoppingList.getSize() != 0) {
                if (isNewList) {
                    intent.putExtra("addList", true);
                } else {
                    intent.putExtra("addList", false);
                    intent.putExtra("position", editListPosition);
                }
            } else {
                intent.putExtra("addList", false);
                intent.putExtra("emptyList", true);
            }
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // Handle 'Next' and 'Done' keys on keyboard
    // Add item to list

    void handleDoneButton() {
        // Inputs
        final TextView addItem = (TextView) findViewById(R.id.addItem);
        final TextView addQty = (TextView) findViewById(R.id.addQty);

        // Keyboard 'done' on addItem gives addQty focus
        addItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    addQty.requestFocus();
                }
                return false;
            }
        });

        // Keyboard 'done' on addQty adds item to list, clears text and gives addItem focus again
        addQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    // Amount default to 1 if none entered
                    int amount;
                    try {
                        amount = Integer.parseInt(addQty.getText().toString());
                    }
                    catch (NumberFormatException e) {
                        amount = 1;
                    }

                    String item = addItem.getText().toString();
                    mListItems.add(new ShoppingListItem(item, amount));
                    updateList();
                    addItem.setText("");
                    addQty.setText("");
                    addItem.requestFocus();
                }
                return false;
            }
        });
    }

    void updateList() {
        mAdapter.notifyDataSetChanged();
    }

    // Prepare list for sending back to MainActivity

    void prepareSendShoppingList() {
        mShoppingList.removeAllItems();
        for (int i = 0; i< mListItems.size(); i++) {
            mShoppingList.addItem(mListItems.get(i));
        }
    }

    // Uncheck all

    void uncheckAllItems() {
        for (int i=0; i<mListItems.size(); i++) {
            mListItems.get(i).uncheckItem();
        }
        updateList();
    }
}

