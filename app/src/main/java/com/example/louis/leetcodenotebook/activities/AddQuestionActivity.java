package com.example.louis.leetcodenotebook.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

    private LinearLayout mTabButtonManul;
    private LinearLayout mTabButtonServer;
    private ActionMenuItemView buttonSaveQuestion;

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
        mToolbar.setTitle("Add question");//设置主标题
//        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));//设置主标题颜色
//        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，




        mToolbar.inflateMenu(R.menu.add_question_manul_menu);//设置右上角的填充菜单
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.button_save_question) {
                    System.out.println("fffffffffffffffff");
                    insertQuestion();
                }
                return true;
            }
        });
        buttonSaveQuestion = (ActionMenuItemView) mToolbar.findViewById(R.id.button_save_question);
        buttonSaveQuestion.setEnabled(false);


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
                                .setImageResource(R.drawable.tab_question_pressed);
                        buttonSaveQuestion.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        ((ImageButton) mTabButtonServer.findViewById(R.id.btn_tab_bottom_server))
                                .setImageResource(R.drawable.tab_answer_pressed);
                        buttonSaveQuestion.setVisibility(View.INVISIBLE);
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
                .setImageResource(R.drawable.tab_question_normal);
        ((ImageButton) mTabButtonServer.findViewById(R.id.btn_tab_bottom_server))
                .setImageResource(R.drawable.tab_answer_normal);
    }

//    public void onLevelSelected(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.radio_level_easy:
//                if (checked)
//                    level = 1;
//                    break;
//            case R.id.radio_level_medium:
//                if (checked)
//                    level = 2;
//                    break;
//            case R.id.radio_level_hard:
//                if (checked)
//                    level = 3;
//                    break;
//        }
//        System.out.println("===> " + level);
//    }


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

        if (questionHelper.getWritableDatabase().insert(DB_TABLE_NAME_QUESTIONS, null, questionValues) > 0) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Add question failed", Toast.LENGTH_LONG).show();
        }


//        for (String k : questionInfo.keySet()) {
//            System.out.println(k + " : " + questionInfo.get(k).length());
//        }
//        questionsHelper.getReadableDatabase().execSQL("insert into questions_table values(null, ?, ?, ?, ?, ?)", new String[]{"two sum", "2", "2 to target", "hint?", "fffffff"});
//        questionsHelper.getReadableDatabase().execSQL("insert into questions_table values(null, ?, ?, ?, ?, ?)", testQuestion);
//
//        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from questions_table", null);
//        inflateListView(cursor);           //刷新listview
    }

}
