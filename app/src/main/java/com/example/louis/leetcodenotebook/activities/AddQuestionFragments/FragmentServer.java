package com.example.louis.leetcodenotebook.activities.AddQuestionFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.R;

/**
 * Created by Louis on 10/8/2017.
 */

public class FragmentServer extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
//        textViewAnswer = (TextView) view.findViewById(R.id.textview_answer);
//        textViewAnswer.setText(questionData.get("answer"));
//        textViewAnswer.setTextIsSelectable(true);

        return view;
    }
}
