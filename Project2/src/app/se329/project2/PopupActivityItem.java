package app.se329.project2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import app.se329.project2.model.InventoryItem;

/**
 * Manages adding/editing an item.
 */
class ItemPopup extends Popup {

	boolean isNewItem;
	int position;
	InventoryItem item;
	
	public ItemPopup(boolean isNew, InventoryItem item, int pos) {
		this.item = item;
		position = pos;
		isNewItem = isNew;
	}

	@Override
	public View getPopupContentView() {
		View popupContent = LayoutInflater.from(popupActivity).inflate(R.layout.popup_item, null, false);
		configureButtonPresses(popupContent);
		
		
		if(!isNewItem) {
			
			inflateTextFields(popupContent, false); // inflate with item data
			
			//hide soft keyboard
			popupActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			//display edit button
			ImageButton editButt = (ImageButton) popupContent.findViewById(R.id.edit_butt);
			editButt.setVisibility(View.VISIBLE);
			editButt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					inflateTextFields(popupActivity.getPopupContent(), true);
					popupActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				}
			});
			
			// display delete button
			ImageButton deleteButt = (ImageButton) popupContent.findViewById(R.id.del_butt);
			deleteButt.setVisibility(View.VISIBLE);
			deleteButt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(popupActivity, R.style.Theme_AppCompat));
					alertDialogBuilder.setTitle("Delete Item");
					alertDialogBuilder
						.setMessage("Are you sure you want to remove this item?")
						.setCancelable(false)
						.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent data = new Intent();
								data.putExtra("to_delete", position);
								closePopup(666, data);
							}
						})
						.setNegativeButton("Cancel", null);
					alertDialogBuilder.create().show();
					
				}
			});
		}

		return popupContent;
	}
	
	private void inflateTextFields(View popupContent, boolean enabled) {
		TextView title = (TextView) popupContent.findViewById(R.id.title_textview);
		title.setText("Item " + (position+1)+":");
		
		EditText itemName = (EditText) popupContent.findViewById(R.id.item_name_field);
		itemName.setEnabled(enabled);
		itemName.requestFocus();
		itemName.setText(item.getName());
		
		EditText itemDesc = (EditText) popupContent.findViewById(R.id.item_descr_field);
		itemDesc.setEnabled(enabled);
		itemDesc.setText(item.getDescr());
		
		EditText itemQuantity = (EditText) popupContent.findViewById(R.id.item_quantity_field);
		itemQuantity.setEnabled(enabled);
		itemQuantity.setText(""+item.getQuantity());
		
		EditText itemWeight = (EditText) popupContent.findViewById(R.id.item_weight_field);
		itemWeight.setEnabled(enabled);
		itemWeight.setText(""+item.getUnitWeight());
		
		EditText itemWeightUnits = (EditText) popupContent.findViewById(R.id.item_weight_unit_field);
		itemWeightUnits.setEnabled(enabled);
		itemWeightUnits.setText(""+item.getWeightUnits());
		
	}
	
	public void configureButtonPresses(View popupContent) {
		
		popupContent.findViewById(R.id.okay_butt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText name = (EditText) popupActivity.findViewById(R.id.item_name_field);
				EditText desc = (EditText) popupActivity.findViewById(R.id.item_descr_field);
				EditText quan = (EditText) popupActivity.findViewById(R.id.item_quantity_field);
				EditText weigh = (EditText) popupActivity.findViewById(R.id.item_weight_field);
				EditText weightU = (EditText) popupActivity.findViewById(R.id.item_weight_unit_field);
				
				int quantity = 0;
				double weight = 0.0;
				try{
					quantity = Integer.parseInt(quan.getText().toString());
					weight = Double.parseDouble(weigh.getText().toString());
				}catch (NumberFormatException e){}
				
				
				item = new InventoryItem(name.getText().toString(), desc.getText().toString(), quantity, weight, weightU.getText().toString());
				
				Intent data = new Intent();
				data.putExtra("item", item);
				closePopup(777, data);
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
