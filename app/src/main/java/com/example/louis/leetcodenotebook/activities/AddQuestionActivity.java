package com.example.louis.leetcodenotebook.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;
import com.example.louis.leetcodenotebook.activities.AddQuestionFragments.FragmentManul;
import com.example.louis.leetcodenotebook.activities.AddQuestionFragments.FragmentServer;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis on 10/8/2017.
 */

public class AddQuestionActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private Toolbar mToolbar;
    private FragmentManul fragmentManul;
    private FragmentServer fragmentServer;

    private QuestionsDatabaseHelper questionHelper;
    private final String DB_NAME_QUESTIONS = "questions";
    private final String DB_TABLE_NAME_QUESTIONS = "questions_table";
    private final String TABLE_NAME_TAG = "tag_table";
    private final String TABLE_NAME_QUESTION_TAG = "question_tag_table";

    private LinearLayout mTabButtonManul;
    private LinearLayout mTabButtonServer;
    private ImageButton buttonSaveQuestion;
    private ImageButton buttonBack;

    private int level = 1;  // easy - 1, medium - 2, hard - 3


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initView();
        initDB();
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_add_question);
        buttonSaveQuestion = (ImageButton) mToolbar.findViewById(R.id.toolbar_save_button);
        buttonSaveQuestion.setEnabled(false);    // Save button disabled as default, a question need at least a title to be created
        buttonSaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertQuestion();
            }
        });

        buttonBack = (ImageButton) mToolbar.findViewById(R.id.toolbar_back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (fragmentManul.isEditting() || fragmentServer.isServerRunning()) {
                new AlertDialog.Builder(AddQuestionActivity.this)
                        .setTitle("Stop Adding")   // Set title
                        .setIcon(android.R.drawable.ic_dialog_info)   // Set window Icon
                        .setMessage("You have unsaved question or the server is running, are you sure you want to leave? ")  // Set message
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick( DialogInterface dialoginterface, int i) {
                                    finish();
                                    }
                                }
                        )
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {}     // dismiss
                                }).show();
            } else {
                finish();
            }

//                if (mViewPager.getCurrentItem() == 0) {
//                    if (fragmentManul.isEditting()) {
//                        new AlertDialog.Builder(AddQuestionActivity.this)
//                                .setTitle("Unsaved Changes")   // Set title
//                                .setIcon(android.R.drawable.ic_dialog_info)   // Set window Icon
//                                .setMessage("You have unsaved changes, are you sure you want to discard them?")  // Set message
//                                .setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick( DialogInterface dialoginterface, int i) {
//                                            finish();
//                                            }
//                                        }
//                                )
//                                .setNegativeButton("No",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(
//                                                    DialogInterface dialoginterface, int i) {}     // dismiss
//                                        }).show();
//                    } else {
//                        finish();
//                    }
//                } else {
//                    if (fragmentServer.isServerRunning()) {
//                        new AlertDialog.Builder(AddQuestionActivity.this)
//                                .setTitle("Stop Server")   // Set title
//                                .setIcon(android.R.drawable.ic_dialog_info)   // Set window Icon
//                                .setMessage("The server is running, are you sure you want to stop it?")  // Set message
//                                .setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick( DialogInterface dialoginterface, int i) {
//                                                fragmentServer.stopServer();
//                                                finish();
//                                            }
//                                        }
//                                )
//                                .setNegativeButton("No",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(
//                                                    DialogInterface dialoginterface, int i) {}     // dismiss
//                                        }).show();
//                    } else {
//                        finish();
//                    }
//                }
            }
        });

       // mToolbar.setTitle("Add question");//设置主标题
//        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));//设置主标题颜色
//        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，

//        mToolbar.inflateMenu(R.menu.add_question_manul_menu);//设置右上角的填充菜单
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int menuItemId = item.getItemId();
//                if (menuItemId == R.id.button_save_question) {
//                    insertQuestion();
//                }
//                return true;
//            }
//        });
//        buttonSaveQuestion = (ActionMenuItemView) mToolbar.findViewById(R.id.button_save_question);
//        buttonSaveQuestion.setEnabled(false);    // Save button disabled as default, a question need at least a title to be created


        mTabButtonManul = (LinearLayout) findViewById(R.id.id_tab_bottom_manul);
        mTabButtonManul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        mTabButtonServer = (LinearLayout) findViewById(R.id.id_tab_bottom_server);
        mTabButtonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        fragmentManul = new FragmentManul();
        fragmentManul.setButton(buttonSaveQuestion);
        fragmentServer = new FragmentServer();
        fragmentServer.setWifiManager((WifiManager) getSystemService(WIFI_SERVICE));
        mFragments.add(fragmentManul);
        mFragments.add(fragmentServer);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_add_question);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentIndex;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                resetTabBtn();
                switch (position)
                {
                    case 0:
                        ((ImageButton) mTabButtonManul.findViewById(R.id.btn_tab_bottom_manul))
                                .setImageResource(R.drawable.ic_tab_manul_pressed);
                        ((TextView) mTabButtonManul.findViewById(R.id.text_tab_bottom_manul)).setTextColor(getResources().getColor(R.color.iconPressed));
                        buttonSaveQuestion.setVisibility(View.VISIBLE);
                        ((TextView) mToolbar.findViewById(R.id.toolbar_add_question_title)).setText("Add Question");
                        break;
                    case 1:
                        ((ImageButton) mTabButtonServer.findViewById(R.id.btn_tab_bottom_server))
                                .setImageResource(R.drawable.ic_tab_server_pressed);
                        ((TextView) mTabButtonServer.findViewById(R.id.text_tab_bottom_server)).setTextColor(getResources().getColor(R.color.iconPressed));
                        buttonSaveQuestion.setVisibility(View.INVISIBLE);
                        ((TextView) mToolbar.findViewById(R.id.toolbar_add_question_title)).setText("Browser Adding");

                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetTabBtn() {
        ((ImageButton) mTabButtonManul.findViewById(R.id.btn_tab_bottom_manul))
                .setImageResource(R.drawable.ic_tab_manul_normal);
        ((TextView) mTabButtonManul.findViewById(R.id.text_tab_bottom_manul)).setTextColor(getResources().getColor(R.color.iconPrimary));

        ((ImageButton) mTabButtonServer.findViewById(R.id.btn_tab_bottom_server))
                .setImageResource(R.drawable.ic_tab_server_normal);
        ((TextView) mTabButtonServer.findViewById(R.id.text_tab_bottom_server)).setTextColor(getResources().getColor(R.color.iconPrimary));
    }


    private void initDB() {
        questionHelper = new QuestionsDatabaseHelper(getApplicationContext(), DB_NAME_QUESTIONS, 1);
    }


    private void insertQuestion() {
        Map<String, String> questionInfo = fragmentManul.getInput();
        ContentValues questionValues = new ContentValues();
        questionValues.put("title", questionInfo.get("title"));
        questionValues.put("level", questionInfo.get("level"));
        questionValues.put("hint", questionInfo.get("hint"));
        questionValues.put("description", questionInfo.get("description"));
        questionValues.put("answer", questionInfo.get("answer"));
        questionValues.put("note", questionInfo.get("note"));

        long insertId = questionHelper.getWritableDatabase().insert(DB_TABLE_NAME_QUESTIONS, null, questionValues);
        if (insertId > 0) {
            // insert question's tags
            List<String> tagNames = fragmentManul.getTags();
            List<Integer> tagIds = new ArrayList<>();

            Cursor cursor = questionHelper.getReadableDatabase().query(TABLE_NAME_TAG, new String[]{"_id, name"}, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                if (tagNames.contains(cursor.getString(1))) {
                    tagIds.add(cursor.getInt(0));
                }
            }

            for (int tagId : tagIds) {
                ContentValues questionTag = new ContentValues();
                questionTag.put("tag_id", tagId);
                questionTag.put("question_id", insertId);
                questionHelper.getWritableDatabase().insert(TABLE_NAME_QUESTION_TAG, null, questionTag);
            }

            // return to the main activity
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Add question failed", Toast.LENGTH_LONG).show();
        }

    }

}
