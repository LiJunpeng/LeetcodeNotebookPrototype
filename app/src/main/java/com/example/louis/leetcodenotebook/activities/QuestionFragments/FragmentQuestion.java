package com.example.louis.leetcodenotebook.activities.QuestionFragments;

import android.database.Cursor;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;


/**
 * Created by Louis on 10/6/2017.
 */

public class FragmentQuestion extends Fragment {

    private TextView title;
    private TextView level;
    private TextView description;
    private TextView hint;
    private TagFlowLayout mFlowLayout;

    private Map<String, String> questionData;
    private List<String> tagList = new ArrayList<>();
    final String DB_NAME_QUESTIONS = "questions";
    final String TABLE_NAME_QUESTION_TAG = "question_tag_table";
    private QuestionsDatabaseHelper questionsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        title = (TextView) view.findViewById(R.id.textview_title);
        level = (TextView) view.findViewById(R.id.textview_level);
        description = (TextView) view.findViewById(R.id.textview_description);
        hint = (TextView) view.findViewById(R.id.textview_hint);
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout_static_tag);

        title.setText(questionData.get("title"));
        level.setText(questionData.get("level"));
        description.setText(questionData.get("description"));
        hint.setText(questionData.get("hint"));

        tagList.add("test");  // test
//        questionsHelper = new QuestionsDatabaseHelper(getActivity(), DB_NAME_QUESTIONS, 1);
//        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from " + TABLE_NAME_QUESTION_TAG + "where question_id = " + , null);
//        while (cursor.moveToNext()) {
//            tagList.add(cursor.getString());
//        }
        final LayoutInflater mInflater = LayoutInflater.from(view.getContext());
        mFlowLayout.setAdapter(new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_static,
                        mFlowLayout, false);
                tv.setText(s);

                return tv;
            }
        });
        return view;
    }


    public void setData(Map<String, String> data) {
        this.questionData = data;
    }
}
