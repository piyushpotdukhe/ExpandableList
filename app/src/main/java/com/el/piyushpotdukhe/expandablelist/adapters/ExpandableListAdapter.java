package com.el.piyushpotdukhe.expandablelist.adapters;

/**
 * Created by piyush.potdukhe on 4/4/2017.
 */

import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.R;

import android.app.Activity;
import android.content.Context;
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
    private List<String> testCases;

    public ExpandableListAdapter(Activity context, List<String> testCases,
                                 Map<String, List<String>> laptopCollection) {
        this.context = context;
        this.testCaseCollection = laptopCollection;
        this.testCases = testCases;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return testCaseCollection.get(testCases.get(groupPosition)).get(childPosition);
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

        TextView item = (TextView) convertView.findViewById(R.id.laptop);

        // this is on click of image view, replacing with chkbox
        ImageView chkbox_img = (ImageView) convertView.findViewById(R.id.chkbox_img);
        chkbox_img.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            }
        });
        item.setText(laptop);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return testCaseCollection.get(testCases.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return testCases.get(groupPosition);
    }

    public int getGroupCount() {
        return testCases.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
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
}
