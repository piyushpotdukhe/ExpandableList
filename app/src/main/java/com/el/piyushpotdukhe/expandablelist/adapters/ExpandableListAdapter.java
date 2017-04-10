package com.el.piyushpotdukhe.expandablelist.adapters;

/**
 * Created by piyush.potdukhe on 4/4/2017.
 */

import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

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
        final String tcName = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.child_check_box);
        checkBox.setText(tcName);
        checkBox.setTextColor(Color.GRAY);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "Child Name: " + buttonView.getText()
                        +" : "+ isChecked, Toast.LENGTH_SHORT).show();
            }
        });

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
                             View convertView, final ViewGroup parent) {
        String groupName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_item, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.group_item_check_box);
        checkBox.setText(groupName);
        checkBox.setTextColor(Color.BLACK);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "Group Name: " + buttonView.getText()
                        +" : "+ isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
