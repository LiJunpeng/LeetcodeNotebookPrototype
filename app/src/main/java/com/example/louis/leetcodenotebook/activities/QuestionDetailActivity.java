package com.example.louis.leetcodenotebook.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;
import com.example.louis.leetcodenotebook.activities.QuestionFragments.FragmentAnswer;
import com.example.louis.leetcodenotebook.activities.QuestionFragments.FragmentQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis on 10/6/2017.
 */

public class QuestionDetailActivity extends FragmentActivity {

    private final int QUESTION_EDITTING_REQUEST = 1;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private LinearLayout mTabBtnQuestion;
    private LinearLayout mTabBtnAnswer;
    private Toolbar myToolbar;
    private ImageButton imageButtonToolbarBack;
    private Button buttonEdit;

    final String DB_NAME_QUESTIONS = "questions";
    private QuestionsDatabaseHelper questionsHelper;
    private Map<String, String> questionData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int questionID = bundle.getInt("questionID");
        questionsHelper = new QuestionsDatabaseHelper(getApplicationContext(), DB_NAME_QUESTIONS, 1);
        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from questions_table where _id = ?", new String[]{String.valueOf(questionID)});
        cursor.moveToNext();

        questionData = new HashMap<>();
        questionData.put("question_id", String.valueOf(cursor.getInt(0)));
        questionData.put("title", cursor.getString(1));
        questionData.put("level", cursor.getString(2));
        questionData.put("description", cursor.getString(3));
        questionData.put("hint", cursor.getString(4));
        questionData.put("answer", cursor.getString(5));
        questionData.put("note", cursor.getString(6));

        initView();

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);


        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            private int currentIndex;

            @Override
            public void onPageSelected(int position)
            {
                resetTabBtn();
                switch (position)
                {
                    case 0:
                        ((ImageButton) mTabBtnQuestion.findViewById(R.id.btn_tab_bottom_question))
                                .setImageResource(R.drawable.ic_tab_question_pressed);
                        ((TextView) mTabBtnQuestion.findViewById(R.id.text_tab_bottom_question)).setTextColor(getResources().getColor(R.color.iconPressed));
                        ((TextView) myToolbar.findViewById(R.id.toolbar_question_detail_title)).setText(getResources().getString(R.string.question_menu_title));
                        break;
                    case 1:
                        ((ImageButton) mTabBtnAnswer.findViewById(R.id.btn_tab_bottom_answer))
                                .setImageResource(R.drawable.ic_tab_answer_pressed);
                        ((TextView) mTabBtnAnswer.findViewById(R.id.text_tab_bottom_answer)).setTextColor(getResources().getColor(R.color.iconPressed));
                        ((TextView) myToolbar.findViewById(R.id.toolbar_question_detail_title)).setText(getResources().getString(R.string.answer_menu_title));
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.question_detail_menu, menu);
//        myToolbar.setTitle(getResources().getString(R.string.question_menu_title));
//        myToolbar.setTitleTextColor(getResources().getColor(R.color.iconPrimary));
//        myToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_button)); // to do
//
//        return true;
//    }


    protected void resetTabBtn()
    {
        ((ImageButton) mTabBtnQuestion.findViewById(R.id.btn_tab_bottom_question))
                .setImageResource(R.drawable.ic_tab_question_normal);
        ((TextView) mTabBtnQuestion.findViewById(R.id.text_tab_bottom_question)).setTextColor(getResources().getColor(R.color.iconPrimary));

        ((ImageButton) mTabBtnAnswer.findViewById(R.id.btn_tab_bottom_answer))
                .setImageResource(R.drawable.ic_tab_answer_normal);
        ((TextView) mTabBtnAnswer.findViewById(R.id.text_tab_bottom_answer)).setTextColor(getResources().getColor(R.color.iconPrimary));
    }

    private void initView()
    {
        myToolbar = (Toolbar) findViewById(R.id.toolbar_question_detail);
        //myToolbar.setTitle(getResources().getString(R.string.question_menu_title));
        //myToolbar.setTitleTextColor(getResources().getColor(R.color.iconPrimary));

        imageButtonToolbarBack = (ImageButton) myToolbar.findViewById(R.id.toolbar_back_button);
        imageButtonToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonEdit = (Button) findViewById(R.id.button_edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditting();
            }
        });

        mTabBtnQuestion = (LinearLayout) findViewById(R.id.id_tab_bottom_question);
        mTabBtnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        mTabBtnAnswer = (LinearLayout) findViewById(R.id.id_tab_bottom_answer);
        mTabBtnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        FragmentQuestion questionFragment = new FragmentQuestion();
        questionFragment.setData(questionData);
        FragmentAnswer fragmentAnswer = new FragmentAnswer();
        fragmentAnswer.setData(questionData);
        mFragments.add(questionFragment);
        mFragments.add(fragmentAnswer);
    }


    private void startEditting() {
        Intent intentEditting = new Intent(QuestionDetailActivity.this, EditQuestionActivity.class);
        intentEditting.putExtra("question_id", questionData.get("question_id"));
        startActivityForResult(intentEditting, QUESTION_EDITTING_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (QUESTION_EDITTING_REQUEST) : {
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("====> finish editting");
//                    tagsFilter = data.getStringArrayListExtra("tags");     // update filter
//                    levelsFilter = data.getIntegerArrayListExtra("level");  // update filter
                }
                break;
            }
        }
    }


}


