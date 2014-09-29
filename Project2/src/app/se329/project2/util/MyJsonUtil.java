package app.se329.project2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import app.se329.project2.model.Inventory;
import app.se329.project2.model.InventoryItem;

public class MyJsonUtil {

	Context cntxt;
	
	public MyJsonUtil(Context context){
		cntxt = context;
	}
	
	public JSONObject readFromFile(String filename){
		String jsonText = null;
		JSONObject jObj = null;
		try{
			FileInputStream is = cntxt.openFileInput(filename);
			byte [] buffer = new byte[is.available()];
			while (is.read(buffer) != -1);
			jsonText = new String(buffer);
			Log.i("JSON", filename+" contents: " + jsonText);
		}
		catch(FileNotFoundException e){
			Log.e("JSON", filename+" not found. Starting a new.");
			jsonText = "{\""+filename+"\":[]}";
		}
		catch(IOException e){
			e.printStackTrace();
		}

		try { jObj = new JSONObject(jsonText); } 
		catch (JSONException e){e.printStackTrace(); }

		return jObj;
	}
	
	public boolean saveToFile(String filename, JSONObject fullDataObj){
		try
		{
			FileOutputStream fos = cntxt.openFileOutput(filename, Context.MODE_PRIVATE);
		    fos.write(fullDataObj.toString().getBytes());
		    fos.close();
		    Log.i("JSON", "File: " +filename+".json updated.\nNew Contents: " + fullDataObj.toString());
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Writes the user credentials to local_users.json file, for local log on
	 * if the user is not already in the file.
	 * @param userToEnter
	 * @param passToEnter
	 * @return True if user is new to this device.
	 */
	public boolean verifyLocalUser(String userToEnter, String passToEnter){
		String filename = "local_users";
		
		//get current file contents
		JSONObject fullDataObj = readFromFile(filename);
		
		// create new user entry
		JSONArray usersArr = null;
		JSONObject newUserObj = null;
		try {
			usersArr = fullDataObj.getJSONArray(filename);
			for(int i = 0 ; i < usersArr.length(); i++)
			{
				newUserObj = usersArr.getJSONObject(i);
				if(newUserObj.getString("username").equals(userToEnter))
				{
					Log.i("JSON", "Local User Found.");
					return false;
				}
			}
			newUserObj = new JSONObject();
			newUserObj.put("username", userToEnter);
			newUserObj.put("password", passToEnter); 
			
			// place new entry into array
			usersArr.put(newUserObj);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// save all the contents
		saveToFile(filename, fullDataObj);
		return true;
	}

	public ArrayList<InventoryItem> getInventoryItems(String userName, String inventoryName) {
		ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();
		
		String filename = userName + "_inv_" + inventoryName;
		
		//get current file contents
		JSONObject inventJson = readFromFile(filename);
		Log.i("JSON", filename + " received: " + inventJson.toString());
		
		// create new user entry
		JSONArray itemsJsonArr = null;
		JSONObject itemJson = null;
		InventoryItem item = null;
		try {
			itemsJsonArr = inventJson.getJSONArray("items");
			for(int i = 0; i < itemsJsonArr.length(); i++){
				item = new InventoryItem();
				itemJson = new JSONObject();
				itemJson = itemsJsonArr.getJSONObject(i);
				item.setName(itemJson.getString("item_name"));
				item.setDescr((itemJson.getString("item_desc")));
				item.setQuantity(Integer.parseInt(itemJson.getString("item_quan")));
				item.setUnitWeight(Double.parseDouble(itemJson.getString("item_weight")));
				
				Log.i("Item", "Added item: " + item.getName());
				items.add(item);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public void writeInventoryItems(Inventory inventory) {
		
		String filename = inventory.getUser() + "_inv_" + inventory.getName();
		ArrayList<InventoryItem> items = inventory.getItems();
		
		// create new user entry
		JSONObject pushInventory = null;
		JSONObject putItem = null;
		JSONArray itemsList = null;
		try {
			pushInventory = new JSONObject();
			itemsList = new JSONArray();
			
			pushInventory.put("name", inventory.getName());
			pushInventory.put("desc", inventory.getDesc());
			for(int i = 0 ; i < items.size(); i ++)
			{
				putItem = new JSONObject();
				putItem.put("item_name", items.get(i).getName());
				putItem.put("item_desc", items.get(i).getDesc());
				putItem.put("item_quan", items.get(i).getQuantity());
				putItem.put("item_weight", items.get(i).getUnitWeight());
				putItem.put("item_weigh_unit", items.get(i).getWeightUnits());
				
				itemsList.put(i, putItem);
			}
			pushInventory.put("items", itemsList);
			
			saveToFile(filename, pushInventory);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
