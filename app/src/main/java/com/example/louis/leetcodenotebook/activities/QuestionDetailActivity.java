package com.example.louis.leetcodenotebook.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


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
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private LinearLayout mTabBtnQuestion;
    private LinearLayout mTabBtnAnswer;

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
        questionData.put("title", cursor.getString(1));
        questionData.put("level", cursor.getString(2));
        questionData.put("description", cursor.getString(3));
        questionData.put("hint", cursor.getString(4));
        questionData.put("answer", cursor.getString(5));

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
                                .setImageResource(R.drawable.tab_question_pressed);
                        break;
                    case 1:
                        ((ImageButton) mTabBtnAnswer.findViewById(R.id.btn_tab_bottom_answer))
                                .setImageResource(R.drawable.tab_answer_pressed);
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

    protected void resetTabBtn()
    {
        ((ImageButton) mTabBtnQuestion.findViewById(R.id.btn_tab_bottom_question))
                .setImageResource(R.drawable.tab_question_normal);
        ((ImageButton) mTabBtnAnswer.findViewById(R.id.btn_tab_bottom_answer))
                .setImageResource(R.drawable.tab_answer_normal);
    }

    private void initView()
    {

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


}
