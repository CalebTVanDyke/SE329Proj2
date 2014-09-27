package app.se329.project2;

import java.io.Serializable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import app.se329.project2.model.InventoryItem;
import app.se329.project2.tools.DatabaseAccess;

/**
 * Manages adding/editing an item.
 */
class ItemPopup extends Popup {

	boolean isNewItem;
	InventoryItem item;
	
	public ItemPopup(boolean isNew, InventoryItem item) {
		this.item = item;
		isNewItem = isNew;
	}

	private void inflateTextFields(View popupContent) {
		EditText itemName = (EditText) popupContent.findViewById(R.id.item_name_field);
		itemName.setText(item.getName());
		
		EditText itemDesc = (EditText) popupContent.findViewById(R.id.item_descr_field);
		itemDesc.setText(item.getDescr());
		
		EditText itemQuantity = (EditText) popupContent.findViewById(R.id.item_quantity_field);
		itemQuantity.setText(item.getQuantity());
		
	}

	@Override
	public View getPopupContentView() {
		View popupContent = LayoutInflater.from(popupActivity).inflate(R.layout.popup_item, null, false);
		configureButtonPresses(popupContent);
		
		if(!isNewItem) {
			inflateTextFields(popupContent);
		}

		return popupContent;
	}
	
	public void configureButtonPresses(View popupContent) {
		
		popupContent.findViewById(R.id.okay_butt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText name = (EditText) popupActivity.findViewById(R.id.item_name_field);
				EditText desc = (EditText) popupActivity.findViewById(R.id.item_descr_field);
				EditText quan = (EditText) popupActivity.findViewById(R.id.item_quantity_field);
				EditText weigh = (EditText) popupActivity.findViewById(R.id.item_weight_field);
				
				int quantity = 0;
				double weight = 0.0;
				try{
					quantity = Integer.parseInt(quan.getText().toString());
					weight = Double.parseDouble(weigh.getText().toString());
				}catch (NumberFormatException e){}
				
				
				item = new InventoryItem(name.getText().toString(), desc.getText().toString(), quantity, weight, "lbs");
				
				Intent data = new Intent();
				data.putExtra("item", item);
				closePopup(69, data);
			}
		});
		
		popupContent.findViewById(R.id.cancel_butt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closePopup();
			}
		});
	}
	
	@Override
	public void popupIsShown(PopupActivity popupActivity) {
		popupActivity.getPopupContent().setVisibility(View.VISIBLE);
	}

	/**
	 * Displays an AlertDialog in the popup activity.
	 * @param title The title of the dialog.
	 * @param message The body text  for the dialog.
	 * @param closePopup Close the registration dialog upon dismissing alert.
	 */
	private void promptUser(String title, String message, final boolean closePopup){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(popupActivity, R.style.Theme_AppCompat));
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder
			.setMessage(message)
			.setCancelable(false)
			.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(closePopup)closePopup();
				}
			});
		alertDialogBuilder.create().show();
	}
}
