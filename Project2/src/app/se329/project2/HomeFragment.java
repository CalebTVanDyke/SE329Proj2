package app.se329.project2;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeFragment extends ProjectFragment{

    View rootView;
    String sessionUser;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		sessionUser = getArguments().getString("sessionUser");
		setupInitial();
		return rootView;
    }
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.go_back).setVisible(false);
	}

	private void setupInitial() {
		TextView tv = (TextView) rootView.findViewById(R.id.user_name_text);
		tv.setText(sessionUser);
		
	}
	
    @Override
    public int getActionBarColor() {
        return Color.TRANSPARENT;
    }
	
	@Override
	protected boolean overrideOnKeyUp(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			if(MainActivity.drawerLayout.isDrawerOpen(Gravity.LEFT))
			{
				MainActivity.drawerLayout.closeDrawers();
				return true;
			}
			
		MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
		return true;
		}
		
		return super.overrideOnKeyUp(keyCode, event);
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
