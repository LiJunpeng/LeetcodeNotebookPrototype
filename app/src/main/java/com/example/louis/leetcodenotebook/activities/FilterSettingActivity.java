package com.example.louis.leetcodenotebook.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Louis on 10/12/2017.
 */

public class FilterSettingActivity extends AppCompatActivity {

    private TagFlowLayout mFlowLayout;
    private Toolbar mToolbar;
    private CheckBox checkboxEasy;
    private CheckBox checkboxMeduim;
    private CheckBox checkboxHard;
    private ImageButton buttonBack;
    private Button buttonApplyFilter;


    private final String DB_NAME_QUESTIONS = "questions";
    private final String TABLE_NAME_QUESTION_TAG = "question_tag_table";
    private final String TABLE_NAME_TAG = "tag_table";
    private QuestionsDatabaseHelper questionsHelper;

    private ArrayList<Integer> level = new ArrayList<>();
    private List<String> tagList = new ArrayList<>();
    private Set<String> tagSelected = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_setting);

        initView();
    }

    private void initView() {
        checkboxEasy = (CheckBox) findViewById(R.id.checkbox_level_easy);
        checkboxMeduim = (CheckBox) findViewById(R.id.checkbox_level_medium);
        checkboxHard = (CheckBox) findViewById(R.id.checkbox_level_hard);

        questionsHelper = new QuestionsDatabaseHelper(getApplication(), DB_NAME_QUESTIONS, 1);
        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from " + TABLE_NAME_TAG, null);
        while (cursor.moveToNext()) {
            tagList.add(cursor.getString(1));
        }

        final LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
        mFlowLayout = (TagFlowLayout) findViewById(R.id.flowlayout_setting_tag);
        mFlowLayout.setAdapter(new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_select, mFlowLayout, false);
                tv.setText(s);

                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);

                tagSelected.add(tagList.get(position));
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);

                tagSelected.remove(tagList.get(position));
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar_filter_setting);

        buttonBack = (ImageButton) findViewById(R.id.toolbar_filter_back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        buttonApplyFilter = (Button) findViewById(R.id.toolbar_filter_apply_button);
        buttonApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                ArrayList<String> tags = new ArrayList<>();
                for (String tag : tagSelected) {
                    tags.add(tag);
                }
                resultIntent.putStringArrayListExtra("tags", tags);

                if (checkboxEasy.isChecked()) level.add(1);
                if (checkboxMeduim.isChecked()) level.add(2);
                if (checkboxHard.isChecked()) level.add(3);
                resultIntent.putIntegerArrayListExtra("level", level);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });


//        mToolbar.setTitle("Setting Filters");
        // mToolbar.inflateMenu(R.menu.filter_setting_menu);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int menuItemId = item.getItemId();
//                if (menuItemId == R.id.button_apply_filter) {
//                    Intent resultIntent = new Intent();
//                    ArrayList<String> tags = new ArrayList<>();
//                    for (String tag : tagSelected) {
//                        tags.add(tag);
//                    }
//                    resultIntent.putStringArrayListExtra("tags", tags);
//
//                    if (checkboxEasy.isChecked()) level.add(1);
//                    if (checkboxMeduim.isChecked()) level.add(2);
//                    if (checkboxHard.isChecked()) level.add(3);
//                    resultIntent.putIntegerArrayListExtra("level", level);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();
//                }
//                return true;
//            }
//        });
    }



//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent resultIntent = new Intent();
//        ArrayList<String> tags = new ArrayList<>();
//        for (String tag : tagSelected) {
//            tags.add(tag);
//        }
//        resultIntent.putStringArrayListExtra("tags", tags);
//        resultIntent.putExtra("level", level);
//        setResult(Activity.RESULT_OK, resultIntent);
//    }


}
