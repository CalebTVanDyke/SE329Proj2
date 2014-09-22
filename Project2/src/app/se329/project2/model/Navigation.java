package app.se329.project2.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import app.se329.project2.MainActivity;
import app.se329.project2.R;

public class Navigation {

    public static String NAVIGATION_ITEMS = "Navigation items";

    public static String[] getItems(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MainActivity.DEFAULT_SHARED_PREF, Context.MODE_PRIVATE);
        return preferences.contains(NAVIGATION_ITEMS) ? getItemsFromPreferences(context, preferences) : getDefaultItems(context, preferences);
    }

    private static String[] getItemsFromPreferences(Context context, SharedPreferences preferences) {
        try {
            JSONArray jsonArray = new JSONArray(preferences.getString(NAVIGATION_ITEMS, "[]"));
            String[] items = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) items[i] = jsonArray.getString(i);
            return ensureAllItemsPresent(context,preferences,items);
        } catch (JSONException e) {
            e.printStackTrace();
            return getDefaultItems(context, preferences);
        }
    }

    private static String[] ensureAllItemsPresent(Context context, SharedPreferences preferences, String[] preferenceItems) {
        // Get default items and test to see if everything is included based on size
        // If it is return preference items
        ArrayList<String> defaultItems = new ArrayList<String>(Arrays.asList(getDefaultItems(context)));

        if (preferenceItems.length == defaultItems.size()) return preferenceItems;

        // Fill resulting items with current preference items
        ArrayList<String> resultingItems = new ArrayList<String>();
        for (String prefItem : preferenceItems){
            if(defaultItems.contains(prefItem))
                resultingItems.add(prefItem);
        }

        // Add the items that are in default but are also not in preferences
        for (String item : defaultItems) {
            if (!Arrays.asList(preferenceItems).contains(item))
                resultingItems.add(item);
        }

        // Saved the new preference items and return them
        String[] resultingItemsStrings = new String[resultingItems.size()];
        for(int i=0;i<resultingItems.size();i++) resultingItemsStrings[i] = resultingItems.get(i);
        saveItemsToPreferences(resultingItemsStrings,preferences);
        return resultingItemsStrings;
    }

    private static String[] getDefaultItems (Context context, SharedPreferences preferences){
        // Get and store the items in shared preferences
        String[] items = getDefaultItems(context);
        saveItemsToPreferences(items,preferences);
        return items;
    }

    private static String[] getDefaultItems (Context context){
        return context.getResources().getStringArray(R.array.menu_items);
    }

    private static void saveItemsToPreferences(String[] items,SharedPreferences preferences){
        JSONArray jsonArray = new JSONArray();
        for (String item : items) jsonArray.put(item);
        preferences.edit().putString(NAVIGATION_ITEMS, jsonArray.toString()).commit();
    }

}
