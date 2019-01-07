package cms.co.in.kat.adapters.ExpandableListAdapter;

/*
  Created by subham_naik on 17-May-17.
 */
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.kat.cws.R;

import cms.co.in.kat.activity.CaseTrack;
import cms.co.in.kat.activity.CaseTrackHeader;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;
    private List<String> childListKey;

    public ExpandableListAdapter(Activity context) {
        this.context = context;

    }

    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Nullable
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, @Nullable View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView value = (TextView) convertView.findViewById(R.id.value);
        TextView keytitle = (TextView) convertView.findViewById(R.id.key);
        View line=(View)convertView.findViewById(R.id.line);

        keytitle.setText(""+childListKey.get(childPosition));
        if(childListKey.get(childPosition).equalsIgnoreCase("case number")) {
            value.setTextColor(context.getResources().getColor(R.color.blue_light));
            value.setTypeface(null, Typeface.BOLD);
        }else{
            value.setTextColor(context.getResources().getColor(R.color.gray));
            value.setTypeface(null, Typeface.NORMAL);

        }
            value.setText(laptop);
        value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childListKey.get(childPosition).equalsIgnoreCase("case number")){

                    String caseno=(String) getChild(groupPosition, childPosition);
                    Intent i = new Intent(context, CaseTrackHeader.class);
                    i.putExtra("caseNo",caseno);

                    context.startActivity(i);
                }
            }
        });

        if((childPosition+1)%6==0){
            line.setVisibility(View.VISIBLE);
        }else{
            line.setVisibility(View.GONE);
        }

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        if(laptopCollections!=null && laptops!=null)
        return laptopCollections.get(laptops.get(groupPosition)).size();
            return 0;
    }

    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public int getGroupCount() {
       if(laptops!=null)
           return laptops.size();
        return 0;

    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Nullable
    public View getGroupView(int groupPosition, boolean isExpanded,
                             @Nullable View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.title);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void updateValue(Activity causeList, List<String> groupList, Map<String, List<String>> laptopCollection, List<String> childListKey) {
        this.context = causeList;
        this.laptops = groupList;
        this.laptopCollections = laptopCollection;
        this.childListKey=childListKey;
    }
}