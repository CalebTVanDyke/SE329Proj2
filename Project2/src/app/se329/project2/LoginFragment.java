package app.se329.project2;

import java.text.StringCharacterIterator;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.se329.project2.model.ProjectContext;
import app.se329.project2.tools.DatabaseAccess;

public class LoginFragment extends ProjectFragment{

	private ProjectContext proContext;
    View rootView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		proContext = ProjectContext.getProjectContext();
		setupInitial(rootView);
		
		return rootView;
    }

    private void loginLocal() {
		
		Log.i("Login", "Attempting Local Login");
	}
	private void loginNet() {
			
		Log.i("Login", "Attempting Net Login");
	}
	private void launchRegisterPopup() {
		
		PopupActivity.closable = true;
		PopupActivity.popup(getActivity(),new RegisterPopup(), false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.go_back).setVisible(false);
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
	
	private void setupInitial(View root){
		((TextView) root.findViewById(R.id.title2)).setTypeface(Typeface.createFromAsset(root.getContext().getAssets(),"fonts/Roboto/Roboto-Light.ttf"));
		root.findViewById(R.id.context_bar).setAnimation(AnimationUtils.loadAnimation(root.getContext(), R.animator.home_screen_animation));
		setHasOptionsMenu(true);
		
		Button localLogin = (Button) rootView.findViewById(R.id.locallogin_butt);
		localLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginLocal();
			}
		});
		Button netLogin = (Button) rootView.findViewById(R.id.netlogin_butt);
		netLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginNet();
			}
		});
		
		Button registerButt = (Button) rootView.findViewById(R.id.signup_butt);
		registerButt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchRegisterPopup();
			}
		});
	}

	class RegisterPopup extends Popup {

		/**
		 * Sets up text for views and listeners for buttons in the popup box.
		 * 
		 * @param popupContent
		 */
		public void setUpPopupViews(View popupContent) {
			
			popupContent.findViewById(R.id.register_user_butt).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					attemptRegister();
				}
			});
			
			popupContent.findViewById(R.id.cancel_reg_butt).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					closePopup();
				}
			});
		}
		
		@Override
		public View getPopupContentView() {
			View popupContent = LayoutInflater.from(getActivity()).inflate(R.layout.popup_register, null, false);
			setUpPopupViews(popupContent);
			return popupContent;
		}

		@Override
		public void popupIsShown(PopupActivity popupActivity) {
			popupActivity.getPopupContent().setVisibility(View.VISIBLE);
		}
		
		private void attemptRegister(){
			
			EditText username = (EditText) popupActivity.findViewById(R.id.username_reg_field);
			EditText pass = (EditText) popupActivity.findViewById(R.id.pass_reg_field);
			EditText passConf = (EditText) popupActivity.findViewById(R.id.conf_pass_field);
			final String userToEnter = username.getText().toString();
			final String passToEnter = pass.getText().toString();
			
			closePopup();
			
			if(pass.length() < 6)
			{
				Toast.makeText(popupActivity, "Passwords must be at least 6 chars.", Toast.LENGTH_LONG).show();
				return;
			}
			if(!pass.getText().toString().equals(passConf.getText().toString()))
			{
				Toast.makeText(popupActivity, "Passwords do not match. Please try again.", Toast.LENGTH_LONG).show();
				return;
			}
			
			Log.i("Register", "Registering User: " + userToEnter + " with pass: " + passToEnter);
			
			new AsyncTask<String, Object, String>() {
				DatabaseAccess dbAccess = new DatabaseAccess(rootView.getContext());
				
				protected void onPreExecute() {
					rootView.findViewById(R.id.reg_loading_home).setVisibility(View.VISIBLE);
				};
				
				@Override
				protected String doInBackground(String... params) {
					
					// query to see if username is available.
					String verifyUser = "Select id from Users where Username = '"+userToEnter+"';";
					String result = dbAccess.query(verifyUser);// should return null if no matches found.
					
					Log.i("Query", "Result: >"+result+"<");
					
					if(result.equals("null\n"))// No users were found. Let's add them.
					{
						Log.i("Register", "---------- Adding User to Users table");
						String insertUser = "INSERT INTO `Users`(`Username`, `Password`)" +
												"VALUES ('"+userToEnter+"','"+passToEnter+"')";
						result = dbAccess.query(insertUser);
						return result;
					}
					return "UAE";// User Already Exists
				}
				
				protected void onPostExecute(String result) {
					rootView.findViewById(R.id.reg_loading_home).setVisibility(View.INVISIBLE);
					
					Log.i("Query Result", "Final Result: " + result);
					if(result.equals("ER"))promptUser("Network Error", "Please check network connection and try again.");
					else if(result.equals("UAE"))promptUser("Registration Error", "User already exists! Please try another.");
					else{
						promptUser("Registration Success", "Please log in to continue.");
						closePopup();
					}
				};
			}.execute();
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
}
