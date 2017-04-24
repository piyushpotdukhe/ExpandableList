package com.el.piyushpotdukhe.expandablelist;

/**
 * Author       : Piyush Potdukhe
 * Date         : 04-04-2017
 * Usage        : Activity Class for expandable list
 */

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.adapters.ExpandableListAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

public class ExpandableListActivity extends AppCompatActivity {

    // <--- schema to maintain the grouping of elements --->
    List<String> groupList; // All group items. Also, used as Key for myCollection map.
    List<String> childList; // individual children list
    Map<String, List<String>> myCollection; // map = { GroupName, ChildList }

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
    } //e.o.onCreate

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("Car");
        groupList.add("Mobile");
        groupList.add("Pen");
    }

    private void createCollection() {
        String[] CarList = {"VW Polo", "Tata Hexa", "Suzuki Swift"};
        String[] MobileList = {"LG G6", "One Plus 3", "Moto G4 Plus"};
        String[] PenList = {"Ball Pen", "Ink Pen", " Roller Pen"};

        myCollection = new LinkedHashMap<>();

        for (String tcGroup : groupList) {
            switch (tcGroup) {
                case "Mobile":
                    loadChild(MobileList);
                    break;
                case "Car":
                    loadChild(CarList);
                    break;
                case "Pen":
                    loadChild(PenList);
                    break;
                default:
                    loadChild(MobileList);
            }
            myCollection.put(tcGroup, childList);
        }
    }

    private void loadChild(String[] testCases) {
        childList = new ArrayList<>();
        childList.addAll(Arrays.asList(testCases));
    }
}
