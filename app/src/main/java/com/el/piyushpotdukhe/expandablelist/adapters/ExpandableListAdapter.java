package com.el.piyushpotdukhe.expandablelist.adapters;

/**
 * Created by piyush.potdukhe on 4/4/2017.
 */

import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> testCaseCollection;
    private List<String> testCaseGroupList;

    public ExpandableListAdapter(Activity context, List<String> tcGroupList,
                                 Map<String, List<String>> tcCollection) {
        this.context = context;
        this.testCaseCollection = tcCollection;
        this.testCaseGroupList = tcGroupList;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return testCaseCollection.get(testCaseGroupList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.child_item);

        // this is on click of image view: just in case if needed in future.
        ImageView chkbox_img = (ImageView) convertView.findViewById(R.id.chkbox_img);
        chkbox_img.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            }
        });
        item.setText(laptop);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return testCaseCollection.get(testCaseGroupList.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return testCaseGroupList.get(groupPosition);
    }

    public int getGroupCount() {
        return testCaseGroupList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.child_item);
        item.setTypeface(null, Typeface.BOLD_ITALIC);
        item.setText(groupName);
        item.setTextColor(Color.BLACK);
//        item.setTextSize(16);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
