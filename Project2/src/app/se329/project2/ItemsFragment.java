package app.se329.project2;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import app.se329.project2.model.InventoryItem;
import app.se329.project2.views.ListItemView;

/**
 * Displays a list of InventoryItems.
 * @author wdArlen
 *
 */
public class ItemsFragment extends ProjectFragment implements OnClickListener {
	
	View rootView;
	private ListView itemsListView;
	private BaseAdapter listViewAdapter;
	private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_items, null,false);
		
		setUpItemsList();
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.items_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			getFragmentManager().popBackStack();
			return true;
		} else if (itemId == R.id.add_item_butt) {
			launchItemPopup(true, null, items.size()+1);// true: new, true: editing, null: not passing in an item.
			return true;
		}
	    return super.onOptionsItemSelected(item);
	}
	
	
	private void setUpItemsList() {
		
		items = getInventoryItems();
		itemsListView = (ListView) rootView.findViewById(R.id.items_list_view);
		listViewAdapter = new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				InventoryItem item = items.get(position);
				ListItemView listItem = new ListItemView(getActivity());
				listItem.setItemName(item.getName());
				//listItem.setItemIcon();
				listItem.setItemSubName(item.getDescr());
				listItem.setItemTextRight(""+item.getQuantity());
				final int pos = position;
				listItem.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						launchItemPopup(false, items.get(pos), pos);
					}
				});
				return listItem;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return items.get(position);
			}
			
			@Override
			public int getCount() {
				return items.size();
			}
		};
		
		itemsListView.setAdapter(listViewAdapter);
		
	}

	private ArrayList<InventoryItem> getInventoryItems() {
		ArrayList<InventoryItem> itemsToReturn = new ArrayList<InventoryItem>();
		
		// dummy data for testing
//		itemsToReturn.add(new InventoryItem("Pizza", "Yummy", 13, 2.4, "lbs"));
//		itemsToReturn.add(new InventoryItem("Shoes", "Running shoes", 4, .57, "lbs"));
//		itemsToReturn.add(new InventoryItem("Beer", "Busch Light 24 pk", 2, 36, "lbs"));
//		itemsToReturn.add(new InventoryItem("TV Remote", "Remote for Samsung", 0, 0.13, "lbs"));
//		itemsToReturn.add(new InventoryItem("Dog", "Mix Breeds", 2, 48, "lbs"));
		
		if(itemsToReturn.size()==0)
			promptUser("Inventory", "It looks like you have no items! Click the add button at the top to create one!");
		return itemsToReturn;
	}
	
	private void launchItemPopup(boolean isNew,  InventoryItem item, int pos){

		PopupActivity.popup(this, getActivity(), 1, new ItemPopup(isNew, item, pos));
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i("Result", "@@@ Returned with resultCode: " + resultCode);
		
		if (requestCode == 1) {
			if(resultCode == 777){ //save new item
				InventoryItem item = (InventoryItem) data.getExtras().getSerializable("item");
				addItem(item);
			}
			else if(resultCode == 666){ //delete item
				int toDelete = data.getExtras().getInt("to_delete");
				items.remove(toDelete);
				
				//TODO Remove from Inventory.
			}
			else{
				Log.i("Item", "Cancel item add/edit. Result Code: "+ resultCode);
			}
			listViewAdapter.notifyDataSetChanged();
		}
	}
	
	private void addItem(InventoryItem item) {
		items.add(item);
		
	}

	@Override
	public void onClick(View v) {
		ListItemView view = (ListItemView) v;
	}
	
	private void promptUser(String title, String message){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder
			.setMessage(message)
			.setCancelable(false)
			.setPositiveButton("Okay", null);
		alertDialogBuilder.create().show();
	}
}
