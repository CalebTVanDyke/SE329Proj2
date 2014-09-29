package app.se329.project2.model;

import java.util.ArrayList;

import android.content.Context;
import app.se329.project2.util.MyJsonUtil;

public class Inventory {

	private String name;
	private String descr;
	private Context cntxt;
	private ArrayList<InventoryItem> items;
	
	private String userName;
	
	
	public Inventory(Context context, String username, String inventoryName){
		cntxt = context;
		userName = username;
		name = inventoryName;
	}
	
	public boolean inflateInventory(String userName, String inventoryName){
		MyJsonUtil jsonUtil = new MyJsonUtil(cntxt);
		items = jsonUtil.getInventoryItems(userName, inventoryName);
		
		return true;
	}
	
	public boolean saveInventory(){
		MyJsonUtil jsonUtil = new MyJsonUtil(cntxt);
		jsonUtil.writeInventoryItems(this);
		
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public ArrayList<InventoryItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<InventoryItem> items) {
		this.items = items;
	}
	
	public String getUser() {
		return userName;
	}

	public void setUser(String user) {
		this.userName = user;
	}
}
