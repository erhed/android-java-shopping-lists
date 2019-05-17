package se.maj7.shoppinglisttwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainListActivity extends AppCompatActivity {

    private static ArrayList<ShoppingList> mShoppingLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private MainListAdapter mAdapter;
    private JSONSerializer mSerializer;
    private static int createDummyLists = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        // Status bar

        setupStatusBar();

        // Toolbar

        setupAppBar();

        // New list-button

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newList();
            }
        });

        // Load data - JSON Serializer

        mSerializer = new JSONSerializer("ShoppingLists.json", getApplicationContext());

        loadShoppingLists();
        sortLists();

        // Add or merge list from EditListActivity

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            Bundle extras = getIntent().getExtras();
            boolean addList = extras.getBoolean("addList");
            boolean emptyList = extras.getBoolean("emptyList");
            int index = extras.getInt("position");

            // Add new list
            if (addList) {
                ShoppingList list = (ShoppingList) intent.getSerializableExtra("shoppingList");
                mShoppingLists.add(list);

                // Sort list, newest first
                sortLists();
            }
            // Remove if edited list is empty
            else if (emptyList) {
                mShoppingLists.remove(index);
            }
            // Save edited list
            else {
                ShoppingList list = (ShoppingList) intent.getSerializableExtra("shoppingList");
                mShoppingLists.set(index, list);
            }

            // Save changes

            saveShoppingLists();
        }

        // RecyclerView setup

        setupRecyclerView();
    }

    // DATA - load/save

    private void loadShoppingLists() {
        try {
            mShoppingLists = mSerializer.load();
        } catch (Exception e) {
            Log.e("Error loading lists: ", "", e);
        }
    }

    public void saveShoppingLists() {
        try {
            mSerializer.save(mShoppingLists);
        } catch(Exception e) {
            Log.e("Error saving lists","", e);
        }
    }

    // Sort list, newest first

    private void sortLists() {
        Collections.sort(mShoppingLists, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList o1, ShoppingList o2) { return o1.getDate().compareTo(o2.getDate()); }
        });
        Collections.reverse(mShoppingLists);
    }

    // Setup view

    private void setupStatusBar() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusBar));

        setSystemBarTheme(this, false);
    }

    private void setupAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingTitle);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarText);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarText);

        float dp = 30;
        float px = dp * getResources().getDisplayMetrics().density;
        collapsingToolbarLayout.setExpandedTitleMarginStart(Math.round(px));
        collapsingToolbarLayout.setTitle("Shopping Lists");
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new MainListAdapter(this, mShoppingLists);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Add dividing line between items in the list
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // Set adapter
        recyclerView.setAdapter(mAdapter);

        // Swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Go to EditListActivity

    void newList() {
        Intent intent = new Intent(this, EditListActivity.class);
        intent.putExtra("isNewList", true);
        startActivity(intent);
    }

    void showList(ShoppingList shoppingList, int position) {
        Intent intent = new Intent(this, EditListActivity.class);
        intent.putExtra("isNewList", false);
        intent.putExtra("position", position);
        intent.putExtra("shoppingList", shoppingList);
        startActivity(intent);
    }

    // Status bar text color

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final void setSystemBarTheme(final Activity pActivity, final boolean backgroundIsDark) {
        final int lFlags = pActivity.getWindow().getDecorView().getSystemUiVisibility();
        // backgroundIsDark = light text
        pActivity.getWindow().getDecorView().setSystemUiVisibility(backgroundIsDark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }

    // Create dummy lists

    void createLists() {
        ShoppingListItem listItem = new ShoppingListItem("Tomater", 1);
        ShoppingListItem listItem2 = new ShoppingListItem("Potatis", 8);
        ShoppingListItem listItem3 = new ShoppingListItem("Banan", 5);
        ShoppingListItem listItem4 = new ShoppingListItem("Grus", 10);

        ShoppingList list1 = new ShoppingList();
        list1.addItem(listItem);
        list1.addItem(listItem2);
        list1.addItem(listItem3);
        list1.addItem(listItem4);

        for (int i=0; i<8; i++) {
            mShoppingLists.add(list1);
        }
    }
}
