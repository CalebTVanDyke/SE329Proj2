package app.se329.project2;

import java.util.ArrayList;

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
			launchItemPopup(true, null);
			return true;
		}
	    return super.onOptionsItemSelected(item);
	}
	
	
	private void setUpItemsList() {
		
		items = getInventoryItems();
		
		itemsListView = (ListView) rootView.findViewById(R.id.extras_list_view);
		itemsListView.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				ListItemView item = new ListItemView(getActivity());
				item.setItemName(items.get(position).getName());
				final int pos = position;
				item.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i("List", items.get(pos).getName()+" pressed...");
					}
				});
				return item;
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
		});
		
	}

	private ArrayList<InventoryItem> getInventoryItems() {
		ArrayList<InventoryItem> itemsToReturn = new ArrayList<InventoryItem>();
		
		// dummy data for testing
		itemsToReturn.add(new InventoryItem("Pizza", "Yummy", 13, 2.4, "lbs"));
		itemsToReturn.add(new InventoryItem("Shoes", "Running shoes", 4, .57, "lbs"));
		itemsToReturn.add(new InventoryItem("Beer", "Bush 24 pk", 2, 36, "lbs"));
		itemsToReturn.add(new InventoryItem("TV Remote", "Remote for Samsung", 0, 0.13, "lbs"));
		itemsToReturn.add(new InventoryItem("Dog", "Mix Breeds", 2, 48, "lbs"));
		
		return itemsToReturn;
	}
	
	private void launchItemPopup(boolean isNew, InventoryItem item){

		PopupActivity.popup(this, getActivity(), 1, new ItemPopup(isNew, item));
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if(resultCode == 69){
				InventoryItem item = (InventoryItem) data.getExtras().getSerializable("item");
				items.add(item);
			}
			else{
				Log.i("Item", "Cancel item add/edit. Result Code: "+ resultCode);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		ListItemView view = (ListItemView) v;
	}
}
