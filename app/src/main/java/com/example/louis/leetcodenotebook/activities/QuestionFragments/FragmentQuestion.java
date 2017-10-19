package com.example.louis.leetcodenotebook.activities.QuestionFragments;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ImageView level;
    private TextView description;
    private TextView hint;
    private TextView note;
    private TagFlowLayout mFlowLayout;

    private int questionId;
    private Map<String, String> questionData;
    private List<String> tagList = new ArrayList<>();
    final String DB_NAME_QUESTIONS = "questions";
    final String TABLE_NAME_QUESTION_TAG = "question_tag_table";
    private final String TABLE_NAME_TAG = "tag_table";
    private QuestionsDatabaseHelper questionsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        title = (TextView) view.findViewById(R.id.textview_title);
        level = (ImageView) view.findViewById(R.id.imageview_level);
        description = (TextView) view.findViewById(R.id.textview_description);
        hint = (TextView) view.findViewById(R.id.textview_hint);
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout_static_tag);
        note = (TextView) view.findViewById(R.id.textview_note);

        title.setText(questionData.get("title"));
        description.setText(questionData.get("description"));
        hint.setText(questionData.get("hint"));
        note.setText(questionData.get("note"));
        questionId = Integer.valueOf(questionData.get("question_id"));
        switch (questionData.get("level")) {
            case "1":
                level.setImageDrawable(getResources().getDrawable(R.drawable.easy_label));
                break;
            case "2":
                level.setImageDrawable(getResources().getDrawable(R.drawable.medium_label));
                break;
            case "3":
                level.setImageDrawable(getResources().getDrawable(R.drawable.hard_label));
                break;
            default:
                break;
        }

        questionsHelper = new QuestionsDatabaseHelper(getActivity(), DB_NAME_QUESTIONS, 1);
        Cursor cursor = questionsHelper.getReadableDatabase().query(TABLE_NAME_QUESTION_TAG, new String[]{"question_id, tag_id"}, "question_id = ?", new String[]{questionData.get("question_id")}, null, null, null, null);
        while (cursor.moveToNext()) {
            Cursor nameCursor = questionsHelper.getReadableDatabase().query(TABLE_NAME_TAG, new String[]{"_id", "name"}, "_id = ?", new String[]{String.valueOf(cursor.getInt(1))}, null, null, null, null);
            nameCursor.moveToFirst();
            tagList.add(nameCursor.getString(1));
        }

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
