package com.example.louis.leetcodenotebook.activities.QuestionFragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.R;

import java.util.Map;

/**
 * Created by Louis on 10/6/2017.
 */

public class FragmentAnswer extends Fragment{

    private Map<String, String> questionData;
    private TextView textViewAnswer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        textViewAnswer = (TextView) view.findViewById(R.id.textview_answer);
        textViewAnswer.setText(questionData.get("answer"));
        textViewAnswer.setTextIsSelectable(true);

        return view;
    }



    public void setData(Map<String, String> data) {
        //System.out.println("====> " + data.get(3));
        this.questionData = data;
//        textViewAnswer.setText(questionData.get("answer"));
    }
}
