package app.se329.project2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import app.se329.project2.model.ProjectContext;
import app.se329.project2.util.InternetUtil;

public class HomeFragment extends ProjectFragment{

	private ProjectContext proContext;
    View rootView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (InternetUtil.getInstanceOfInternetUtil().isNetworkAvailable(getActivity())) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            proContext = ProjectContext.getProjectContext();
            setupInitial(rootView);
            return rootView;
        } else {
            rootView = inflater.inflate(R.layout.fragment_home_alt, container, false);
            rootView.setAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.animator.fade_in_slow));
            return rootView;
        }
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
	}

}
