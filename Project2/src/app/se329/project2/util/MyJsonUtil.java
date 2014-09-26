package app.se329.project2.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

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
			Log.e("JSON", "Local Users File not found. Starting a new.");
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
		    Log.i("JSON", "File: " +filename+".json updated.\nNew Contents: " 
		    										+ fullDataObj.toString());
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
	
}
