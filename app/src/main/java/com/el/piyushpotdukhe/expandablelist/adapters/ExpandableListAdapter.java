package com.el.piyushpotdukhe.expandablelist.adapters;

/**
 * Author       : Piyush Potdukhe
 * Date         : 04-04-2017
 * Usage        : Adapter class for expandable list
 */

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ExpandableListAdapter extends BaseExpandableListAdapter /*implements View.OnClickListener*/ {

    private Activity context; // activity context
    private List<String> groupList; // group
    private Map<String, List<String>> testCaseCollection; // map = { GroupName, ChildList }

    // list to maintain: if user selected the item + group or not. For future use.
    private static Map<String, Boolean> allCBStatus = new LinkedHashMap<>();

    public ExpandableListAdapter(Activity context, List<String> tcGroupList,
                                 Map<String, List<String>> tcCollection) {
        this.context = context;
        this.testCaseCollection = tcCollection;
        this.groupList = tcGroupList;
    }

    private void setNewItems(Activity context, List<String> tcGroupList,
                            Map<String, List<String>> tcCollection) {
        this.context = context;
        this.testCaseCollection = tcCollection;
        this.groupList = tcGroupList;

        // as data set is changed, view for adapter will be reloaded
        // this will help reload the view and hence group can be checked / un-checked
        notifyDataSetChanged();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return testCaseCollection.get(groupList.get(groupPosition)).get(childPosition);
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

        //toggle sub-list, on group status changed.
        checkBox.setEnabled(!getAllCBStatus(getGroupName(tcName)));

        if (checkBox.isChecked() != getAllCBStatus(tcName)) { // refrain additional onChecked calls
            checkBox.setOnCheckedChangeListener(null);// refrain unnecessary checking/unchecking
            checkBox.setChecked(getAllCBStatus(tcName));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String clickedChild = buttonView.getText().toString();
                updateAllCBStatus(clickedChild, isChecked);
                setNewItems(context, groupList, testCaseCollection);
            }
        });

        return convertView;
    }

    private String getGroupName(String childName) {
        for (String groupName : groupList) {
            List<String> childTCs = testCaseCollection.get(groupName);
            if (childTCs.contains(childName)) {
                return groupName;
            }
        }
        return "dummyReturned"; // group not found, return some dummy
    }

    private Boolean areAllChildrenChecked(List<String> childTCs, Boolean value) {
        boolean result = value;
        int totalChildrenChecked = 0;
        for (String childTC : childTCs) {
            if (allCBStatus.containsKey(childTC)) {
                result = result & /*^*/ allCBStatus.get(childTC);
                totalChildrenChecked++;
            }
        }

        if (result) {
            if (totalChildrenChecked != childTCs.size()) {
                result = false;
            } // else keep result as is
        }

        return result;
    }

    private void updateGroupCBStatus(String childTestCaseName) {
        String groupName = getGroupName(childTestCaseName);
        List<String> childTCs = testCaseCollection.get(groupName);

        if (areAllChildrenChecked(childTCs, true /*no option un check*/)) {
            allCBStatus.put(groupName, true);
        }
    }

    private void updateSubListCBStatus(String groupName, Boolean value) {
        List<String> childTCs = testCaseCollection.get(groupName);
        for (String tc : childTCs) {
            allCBStatus.put(tc, value);
        }
    }

    private void updateAllCBStatus(String name, Boolean value) {
        allCBStatus.put(name, value); // add or update the changed value

        if (groupList.contains(name)) { // if name is group
            updateSubListCBStatus(name, value); // set all child same as group
        } else { // if name is not group, it must be child test case
            updateGroupCBStatus(name);
        }
    }

    private boolean getAllCBStatus(String name) {
        if (allCBStatus.containsKey(name)) {
            return allCBStatus.get(name);
        }
        return false;
    }

    public int getChildrenCount(int groupPosition) {
        return testCaseCollection.get(groupList.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    public int getGroupCount() {
        return groupList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded,
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

        if (checkBox.isChecked() != getAllCBStatus(groupName)) { // refrain re-checking/un-checking
            checkBox.setOnCheckedChangeListener(null); // refrain cyclical onCheckedChanged
            checkBox.setChecked(getAllCBStatus(groupName));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateAllCBStatus(buttonView.getText().toString(), isChecked);
                setNewItems(context, groupList, testCaseCollection);
            }

        });
        return convertView;
    }

    /*@Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

    }*/

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}