package com.eads.co.nomad;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class JobCardAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<JobCardData> jobCards;
	private LayoutInflater inflater;

	public JobCardAdapter(Context context, ArrayList<JobCardData> jobCards) {
		this.context = context;
		this.jobCards = jobCards;
		inflater = LayoutInflater.from(context);
	}

	public boolean areAllItemsEnabled() {
		return true;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return jobCards.get(groupPosition).getJobCardDetails().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final JobCardDetails objet = (JobCardDetails) getChild(groupPosition, childPosition);
		
		ChildViewHolder childViewHolder;
		
        if (convertView == null) {
        	childViewHolder = new ChildViewHolder();
        	
            convertView = inflater.inflate(R.layout.job_card, null);
            
            childViewHolder.textViewChildAc = (TextView) convertView.findViewById(R.id.acDetails);
            childViewHolder.textViewChildTask = (TextView) convertView.findViewById(R.id.taskDetails);
            childViewHolder.buttonChild = (Button) convertView.findViewById(R.id.openButton);
            childViewHolder.buttonChild.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x268bd2)); //color/blue
            convertView.setTag(childViewHolder);
        } else {
        	childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        
        childViewHolder.textViewChildAc.setText(objet.getAcDetails());
        childViewHolder.textViewChildTask.setText(objet.getTaskDetails());
        
//        childViewHolder.buttonChild.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				Toast.makeText(context, "Groupe : " + objet.getGroupe().getNom() + " - Bouton : " + objet.getNom(), Toast.LENGTH_SHORT).show();				
//			}
//		});
        
        return convertView;
		
//		ChildViewHolder childViewHolder;
//		
//        if (convertView == null) {
//        	childViewHolder = new ChildViewHolder();
//        	
//            convertView = inflater.inflate(R.layout.job_card, null);
//            
//            childViewHolder.textViewDetails = (TextView) convertView.findViewById(R.id.details);
//            
//            convertView.setTag(childViewHolder);
//        } else {
//        	childViewHolder = (ChildViewHolder) convertView.getTag();
//        }
//        
//        childViewHolder.textViewDetails.setText(objet.getName());
//        
//        return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return jobCards.get(groupPosition).getJobCardDetails().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return jobCards.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return jobCards.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder gholder;
		
		JobCardData group = (JobCardData) getGroup(groupPosition);
		
        if (convertView == null) {
        	gholder = new GroupViewHolder();
        	
        	convertView = inflater.inflate(R.layout.job_card_group, null);
        	
        	gholder.textViewGroup = (TextView) convertView.findViewById(R.id.jobCardGroup);
        	convertView.setTag(gholder);
        } else {
        	gholder = (GroupViewHolder) convertView.getTag();
        }
        
        gholder.textViewGroup.setText(group.getName());
        
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	class GroupViewHolder {
		public TextView textViewGroup;
	}
	
	class ChildViewHolder {
		public TextView textViewChildAc;
		public TextView textViewChildTask;
		public Button buttonChild;
	}

}
