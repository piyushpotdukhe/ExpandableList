package com.el.piyushpotdukhe.expandablelist.adapters;

/**
 * Created by piyush.potdukhe on 4/4/2017.
 */

import java.util.LinkedHashMap;
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

public class ExpandableListAdapter extends BaseExpandableListAdapter /*implements View.OnClickListener*/ {

    private Activity context;
    private Map<String, List<String>> testCaseCollection;
    private List<String> testCaseGroupList;

    public static Map<String, Boolean> allCBStatus = new LinkedHashMap<>();

    public ExpandableListAdapter(Activity context, List<String> tcGroupList,
                                 Map<String, List<String>> tcCollection) {
        this.context = context;
        this.testCaseCollection = tcCollection;
        this.testCaseGroupList = tcGroupList;
    }

    public void setNewItems(Activity context, List<String> tcGroupList,
                            Map<String, List<String>> tcCollection) {
        this.context = context;
        this.testCaseCollection = tcCollection;
        this.testCaseGroupList = tcGroupList;
        notifyDataSetChanged(); // as data set is changed, view for adapter will be reloaded.
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

        //toggle sub-list, on group status changed.
        checkBox.setEnabled(!getAllCBStatus(getGroupName(tcName)));

//        Log.d("ExpandableListAdapter", "getChildView: " + checkBox.getText() + "=" + checkBox.isChecked() );
        if (checkBox.isChecked() != getAllCBStatus(tcName)) { // refrain additional onChecked calls
            checkBox.setOnCheckedChangeListener(null);// refrain unnecessary checking/unchecking
            checkBox.setChecked(getAllCBStatus(tcName));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String clickedChild = buttonView.getText().toString();
//                Log.d("ExpandableListAdapter", "onCheckedChanged-CHILD: " + clickedChild + "=" + isChecked);
                updateAllCBStatus(clickedChild, isChecked);
                setNewItems(context, testCaseGroupList, testCaseCollection);
            }
        });

        return convertView;
    }

    private String getGroupName(String childName) {
        for (String groupName : testCaseGroupList) {
            List<String> childTCs = testCaseCollection.get(groupName);
            if (childTCs.contains(childName)) {
                return groupName;
            }
        }
        return "qwerty"; // group not found, return some dummy
    }

    private Boolean areAllChildrenChecked(List<String> childTCs, Boolean value, String childTestCaseName) {
        boolean result = value;
        int totalChildrenChecked = 0;
        for (String childTC : childTCs) {
            if (allCBStatus.containsKey(childTC)) {
                result = result & /*^*/ allCBStatus.get(childTC);
                totalChildrenChecked++;
            }
        }

        if (true == result) {
            if (totalChildrenChecked != childTCs.size()) {
                result = false;
            } // else keep result as is
        }

        /*Log.d("ExpandableListAdapter", "areAllChildrenChecked=" + result
                + " childTestCaseName=" + childTestCaseName);*/
        return result;
    }

    public void updateGroupCBStatus(String childTestCaseName, Boolean value) {
        String groupName = getGroupName(childTestCaseName);
        List<String> childTCs = testCaseCollection.get(groupName);

        if (areAllChildrenChecked(childTCs, true /*no option uncheck*/, childTestCaseName)) {
            if (allCBStatus.containsKey(groupName)) {
                allCBStatus.remove(groupName);
            }
            allCBStatus.put(groupName, true);
        }
    }

    public void updateSubListCBStatus(String groupName, Boolean value) {
        List<String> childTCs = testCaseCollection.get(groupName);
        for (String tc : childTCs) {
            if (allCBStatus.containsKey(tc)) {
                allCBStatus.remove(tc);
            }
            allCBStatus.put(tc, value);
        }
    }

    public void updateAllCBStatus(String name, Boolean value) {
        if (allCBStatus.containsKey(name)) {
            allCBStatus.remove(name);
        }
        allCBStatus.put(name, value); // add the changed value

        if (testCaseGroupList.contains(name)) { // if name is group
            updateSubListCBStatus(name, value); // set all child same as group
        } else { // if name is not group, it must be child test case
            updateGroupCBStatus(name, value);
        }
    }

    public boolean getAllCBStatus(String name) {
        if (allCBStatus.containsKey(name)) {
            return allCBStatus.get(name);
        }
        return false;
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

        if (checkBox.isChecked() != getAllCBStatus(groupName)) { // refrain unnecessary checking/unchecking
            checkBox.setOnCheckedChangeListener(null); // refrain cyclical onCheckedChanged
            checkBox.setChecked(getAllCBStatus(groupName));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateAllCBStatus(buttonView.getText().toString(), isChecked);
                setNewItems(context, testCaseGroupList, testCaseCollection);
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