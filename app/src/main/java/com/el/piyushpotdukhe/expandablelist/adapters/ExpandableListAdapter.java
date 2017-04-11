package com.el.piyushpotdukhe.expandablelist.adapters;

/**
 * Created by piyush.potdukhe on 4/4/2017.
 */

import java.util.ArrayList;
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
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> testCaseCollection;
    private static Map<String, List<CheckBox>> groupViewHolder = new LinkedHashMap<>();
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
//        Log.d("ExpandableListAdapter", "getChildView");
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.child_check_box);
        checkBox.setText(tcName);
        checkBox.setTextColor(Color.GRAY);

        String grpString = testCaseGroupList.get(groupPosition);
        if (groupViewHolder.containsKey(grpString)) {
            List<CheckBox> childCheckBoxList =  groupViewHolder.get(grpString);
            if (!childCheckBoxList.contains(checkBox)) {
                childCheckBoxList.add(checkBox);
            }
            groupViewHolder.remove(grpString);
            groupViewHolder.put(grpString, childCheckBoxList);
        } else { // this is first time.
            List<CheckBox> childCheckBoxList = new ArrayList<>();
            childCheckBoxList.add(checkBox);
            groupViewHolder.put(grpString, childCheckBoxList);
        }

// code said: i was here.

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

    /*public class GroupViewHolder {
        CheckBox groupcheck;
    }*/



    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        String groupName = (String) getGroup(groupPosition);
//        Log.d("ExpandableListAdapter", "getGroupView");
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

                for (int i=0; i<groupViewHolder.size(); i++) {
                    List<CheckBox> childCheckBoxList = groupViewHolder.get(buttonView.getText());
                    /*Log.d("ExpandableListAdapter", "getChildView, groupViewHolder= {"
                            + " group = " + grpString
                            + " child = " + childCheckBoxList
                            + " }"
                    );*/

                    for (CheckBox cb: childCheckBoxList) {
                        cb.setChecked(isChecked);
                    }
                }
            }

        });

        /*checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<(testCaseCollection.get(groupPosition)).size(); i++) {
                    testCaseCollection.get(groupPosition).get(i).setCheck(gholder.groupcheckbox.isChecked());
                }

                array.get(groupPosition).setCheck(gholder.groupcheck.isChecked());

                notifyDataSetChanged();
            }
        });*/

        return convertView;
    }

    /*@Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        Toast.makeText(context, "onGroupExpanded:groupPosition=" + groupPosition, Toast.LENGTH_SHORT).show();

        if(is_group_checked) {
            check all children.
        }

    }*/

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
