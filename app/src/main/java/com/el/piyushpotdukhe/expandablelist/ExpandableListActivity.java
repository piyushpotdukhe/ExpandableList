package com.el.piyushpotdukhe.expandablelist;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.adapters.ExpandableListAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;

public class ExpandableListActivity extends AppCompatActivity {
    public static final String LOG_TAG = "ExpandableListActivity";

    // <--- schema to maintain the grouping of elements --->
    List<String> groupList; // this is group names list. Used as Key for myCollection map.
    List<String> childList;
    Map<String, List<String>> myCollection; // this is { GroupName, ChildList }

    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        createGroupList();
        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.tc_group);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, myCollection);
        expListView.setAdapter(expListAdapter);
        expListView.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selectedChild = (String) expListAdapter.getChild(
                        groupPosition, childPosition);

                CheckBox childCheckBox = (CheckBox) (((RelativeLayout) v).getChildAt(0));
                childCheckBox.setChecked(!childCheckBox.isChecked());

                return true;
            }
        });
    } //e.o.onCreate

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("VOLTE");
        groupList.add("CS");
        groupList.add("VOWIFI");
    }

    private void createCollection() {
        String[] VoLTE_Cases = {"MO Call", "MTCall", "CLIR"};
        String[] CS_Cases = {"Conference Call", "Video Mo Call"};
        String[] VoWiFi_Cases = {"SMS", "Registration"};

        myCollection = new LinkedHashMap<>();

        for (String tcGroup : groupList) {
            switch (tcGroup){
                case "CS"       : loadChild(CS_Cases)     ; break;
                case "VOLTE"    : loadChild(VoLTE_Cases)  ; break;
                case "VOWIFI"   : loadChild(VoWiFi_Cases) ; break;
                default: loadChild(CS_Cases);
            }
            myCollection.put(tcGroup, childList);
        }
    }

    private void loadChild(String[] testCases) {
        childList = new ArrayList<>();
        childList.addAll(Arrays.asList(testCases));
    }
}
