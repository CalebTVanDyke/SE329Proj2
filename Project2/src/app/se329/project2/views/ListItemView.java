package app.se329.project2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.se329.project2.R;

public class ListItemView extends LinearLayout{
	
	View root;
	TextView textView;
	TextView subTextView;
	ImageView iconImageView;
	ImageView iconImageViewRight;
	
	String attrText;
	
	public ListItemView(Context context) {
		super(context);
		sharedConstructor(context);
	}
	
	public ListItemView(Context context, AttributeSet attrs){
		super(context,attrs);
		sharedConstructor(context);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ListItemView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.ListItemView_text:
                    String text = a.getString(attr);
                    textView.setText(text);
                    break;
            }
        }
        a.recycle();

    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	private void sharedConstructor(Context context){
		root = LayoutInflater.from(context).inflate(R.layout.view_list_item, this);
        if(root!=null) {
            textView = (TextView) root.findViewById(R.id.list_item_textview);
            subTextView = (TextView) root.findViewById(R.id.list_item_subtextview);
            iconImageView = (ImageView) root.findViewById(R.id.list_item_icon);
            iconImageViewRight = (ImageView) root.findViewById(R.id.list_item_icon_right);
        }
	}
	
	public void setItemName(String name){
		textView.setText(name);
	}

	public String getItemName() {
		return textView.getText().toString();
	}
	
	public void setItemIcon(int resource){
		iconImageView.setImageDrawable(getContext().getResources().getDrawable(resource));
		iconImageView.setVisibility(VISIBLE);
	}
	
	public void setItemIcon(String imageUrl){
		
		Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		iconImageView.setImageBitmap(icon);
		iconImageView.setVisibility(VISIBLE);
		
//		new AsyncTask<String, Object, Bitmap>() {
//
//			@Override
//			protected Bitmap doInBackground(String... urls) {
//				int size = iconImageView.getHeight();
//				return InternetUtil.getInstanceOfInternetUtil().getBitmapForUrl(urls[0], size, size);
//			}
//			
//			protected void onPostExecute(Bitmap icon) {
//				iconImageView.setImageBitmap(icon);
//				iconImageView.setVisibility(VISIBLE);
//			};
//			
//		}.execute(imageUrl);
	}
	
	public void setItemIconRight(int resource)
	{
		iconImageViewRight.setImageDrawable(getContext().getResources().getDrawable(resource));
		iconImageViewRight.setVisibility(VISIBLE);
	}
	
	public void reset(){
		subTextView.setVisibility(GONE);
		iconImageView.setVisibility(GONE);
	}
	
	public void setItemSubName(String sub){
		subTextView.setVisibility(VISIBLE);
		subTextView.setText(sub);
	}
	
	public String getItemSubName(){
		return subTextView.getText().toString();
	}
	
	public void setTextwithIndicator(String name)
	{
		this.setItemName(name);
		this.setItemIconRight(R.drawable.ic_disclosure_indicator);
	}
	
	public void setTextWithEmptyStar(String name)
	{
		this.setItemName(name);
		this.setItemIconRight(R.drawable.ic_empty_star);
	}
	
	public void setTextWithFullStar(String name)
	{
		this.setItemName(name);
		this.setItemIconRight(R.drawable.ic_full_star);
	}
}
