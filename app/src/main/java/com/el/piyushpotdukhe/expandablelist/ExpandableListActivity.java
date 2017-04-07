package com.el.piyushpotdukhe.expandablelist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.el.piyushpotdukhe.expandablelist.adapters.ExpandableListAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_SHORT).show();

                ImageView checkBoxImg = (ImageView) (((RelativeLayout) v).getChildAt(1));
                checkBoxImg.setImageDrawable(getToggledCheckBoxImage(checkBoxImg));
                return true;
            }

            private Drawable getToggledCheckBoxImage(ImageView chkbox) {
                Drawable result = chkbox.getDrawable();
                try {
                    Drawable.ConstantState drawableFromImageView = chkbox.getDrawable().getConstantState();
                    Drawable.ConstantState drawableChecked = getDrawable(R.drawable.icon_checked).getConstantState();
                    Drawable.ConstantState drawableUnChecked = getDrawable(R.drawable.icon_unchecked).getConstantState();

                    /*Log.d(LOG_TAG, "drawableFromImageView =" + drawableFromImageView);
                    Log.d(LOG_TAG, "drawableChecked       =" + drawableChecked);
                    Log.d(LOG_TAG, "drawableUnChecked     =" + drawableUnChecked);*/

                    if (drawableFromImageView == drawableChecked) {
                        result = getDrawable(R.drawable.icon_unchecked);
                    } else if (drawableFromImageView == drawableUnChecked) {
                        result = getDrawable(R.drawable.icon_checked);
                    }
                } catch (NullPointerException npe) {
                    Log.e(LOG_TAG, "NullPointerException");
                    npe.printStackTrace();
                    Toast.makeText(getApplicationContext()
                            , "Could not select due to internal exception."
                            , Toast.LENGTH_SHORT).show();
                }

                return result;
            }
        });
    }

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
