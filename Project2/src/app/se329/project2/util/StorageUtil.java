package app.se329.project2.util;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StorageUtil {
	
	static String FILE_NAME = "insert_filename_here.json";
	
	private Activity activity;
	
	public StorageUtil(Activity activity){
		this.activity = activity;
	}
	
	
	/////////////////////////////////
	//       JSON storage        
	/////////////////////////////////
	/**
	 * Get JSON data.
	 * @return JSONObject that contains all of the campus map data.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public JSONObject getJsonForCampusMapLocations() throws JSONException, IOException {
		
		FileInputStream fis = activity.openFileInput(FILE_NAME);
		JSONObject json = new JSONObject(getStringFromInputStream(fis));
		fis.close();
		
		return json;
	
	}

	/**
	 * save given JSON object.
	 * @param categoriesJson JSONObject to be stored in the xml file. 
	 */
	public void saveCampusMapLocationsJson(JSONObject jsonObj) {
		FileOutputStream fos;
		try{
			fos = activity.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			fos.write(jsonToBytes(jsonObj));
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}  
	
	
	/************************* Helper Methods *************************/
	
	private byte[] jsonToBytes(JSONObject json){
		return json.toString().getBytes();
	}
	
	private String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
