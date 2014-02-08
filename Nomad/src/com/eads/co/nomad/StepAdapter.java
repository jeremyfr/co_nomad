package com.eads.co.nomad;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class StepAdapter extends BaseAdapter{

	private List<Step> mStep;
	private LayoutInflater mInf;
	private Button mButton;
	
	
	public StepAdapter(Context c) {
		mInf = LayoutInflater.from(c);
		
	}

	@Override
	public int getCount() {
		
		return mStep.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mStep.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		StepViewHolder h;
		if(arg1 == null){
			
			arg1 = mInf.inflate(R.layout.step,null);
			h = new StepViewHolder();
			h.mTask = (TextView) arg1.findViewById(R.id.textView1);
			arg1.setTag(h);
		}
		else{
			h = (StepViewHolder) arg1.getTag();
		}
		h.setTask(mStep.get(arg0).getText());
		
		return arg1;
	}

	public void setListItems(List<Step> l){
		mStep = l;
	}

	public class StepViewHolder{
		private TextView mTask;
		private Button mButton;
		
		public void setTask(String t){
			mTask.setText(t);
		}
		
		public void setButton(Button b){
			mButton = b;			
		}
	}

	
}


