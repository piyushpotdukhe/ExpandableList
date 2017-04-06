package com.el.piyushpotdukhe.expandablelist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.adapters.ExpandableListAdapter;

import android.app.Activity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ExpandableListActivity extends Activity {

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> tcCollection;
    ExpandableListView expListView;

//    public final String LOG_TAG = "AutomatorListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGroupList();
        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.tc_group);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, tcCollection);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                ImageView chkbox_img = (ImageView) (((RelativeLayout) v).getChildAt(1));
                chkbox_img.setImageDrawable(getSwitchImage(chkbox_img));
                return true;
            }

            private Drawable getSwitchImage(ImageView chkbox) {
                Drawable result = chkbox.getDrawable();

                Drawable.ConstantState drawableFromImageView = chkbox.getDrawable().getConstantState();
                Drawable.ConstantState drawableChecked = getDrawable(R.drawable.icon_checked).getConstantState();
                Drawable.ConstantState drawableUnChecked = getDrawable(R.drawable.icon_unchecked).getConstantState();

                /*Log.d("TestMe", "drawableFromImageView =" + drawableFromImageView);
                Log.d("TestMe", "drawableChecked       =" + drawableChecked);
                Log.d("TestMe", "drawableUnChecked     =" + drawableUnChecked);*/

                if (drawableFromImageView == drawableChecked) {
                    result = getDrawable(R.drawable.icon_unchecked);
                } else if (drawableFromImageView == drawableUnChecked) {
                    result = getDrawable(R.drawable.icon_checked);
                }
                return result;
            }
        });
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("VOLTE");
        groupList.add("CS");
        groupList.add("VOWIFI");
    }

    private void createCollection() {
        String[] VoLTE_Cases = {"MO Call", "MTCall", "CLIR"};
        String[] CS_Cases = {"Conference Call", "Video Mo Call"};
        String[] VoWiFi_Cases = {"SMS", "Registration"};

        tcCollection = new LinkedHashMap<String, List<String>>();

        for (String tcGroup : groupList) {
            if (tcGroup.equals("VOLTE")) {
                loadChild(VoLTE_Cases);
            } else if (tcGroup.equals("CS")) {
                loadChild(CS_Cases);
            } else if (tcGroup.equals("VOWIFI")) {
                loadChild(VoWiFi_Cases);
            } else {
                loadChild(CS_Cases);
            }
            tcCollection.put(tcGroup, childList);
        }
    }

    private void loadChild(String[] testCases) {
        childList = new ArrayList<String>();
        for (String tc : testCases)
            childList.add(tc);
    }
}
