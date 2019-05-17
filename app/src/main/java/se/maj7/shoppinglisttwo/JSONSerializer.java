package se.maj7.shoppinglisttwo;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONSerializer {

    private String mFilename;
    private Context mContext;
    private Gson gson;

    public JSONSerializer(String filename, Context context) {
        mFilename = filename;
        mContext = context;
    }

    public void save(ArrayList<ShoppingList> shoppingLists) throws IOException {
        Gson gson = new Gson();
        String list = gson.toJson(shoppingLists);

        // Write to file
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, mContext.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(list);
            Log.i("JSON save", list);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<ShoppingList> load() throws IOException {
        Gson gson = new Gson();
        BufferedReader reader = null;
        ArrayList<ShoppingList> mList = new ArrayList<>();

        try {
            // Read file
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();

            // Convert to ArrayList<ShoppingList>
            Type shoppingListType = new TypeToken<ArrayList<ShoppingList>>(){}.getType();
            mList = gson.fromJson(json, shoppingListType);
        } catch (FileNotFoundException e) {
            // nada
        } finally {
            if (reader != null) reader.close();
        }

        return mList;
    }
}
