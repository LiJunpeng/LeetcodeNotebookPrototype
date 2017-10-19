package com.example.louis.leetcodenotebook.activities.Dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.DataClass.Tag;
import com.example.louis.leetcodenotebook.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Louis on 10/10/2017.
 */

public class DialogSelectTag extends DialogFragment {

    private TagFlowLayout mTagFlowLayout;
    private Button imageButtonCreateNewTag;
    private EditText editTextCreateNewTag;

    final String DB_NAME_QUESTIONS = "questions";
    final String TABLE_NAME_TAG = "tag_table";
    private QuestionsDatabaseHelper questionsHelper;

    private List<String> tagNames = new ArrayList<>();
    private ArrayList<String> tagToAvoid = new ArrayList<>();
    private Set<String> tagSelected = new HashSet<>()
;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater mInflater = getActivity().getLayoutInflater();

        View dialogView = mInflater.inflate(R.layout.dialog_select_tag, null);
        mTagFlowLayout = (TagFlowLayout) dialogView.findViewById(R.id.flowlayout_select_tag);

        tagToAvoid = getArguments().getStringArrayList("tagToAvoid");
        questionsHelper = new QuestionsDatabaseHelper(getActivity(), DB_NAME_QUESTIONS, 1);
        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from " + TABLE_NAME_TAG, null);
        while (cursor.moveToNext()) {
            String tagToAdd = cursor.getString(1);
            if (!tagToAvoid.contains(tagToAdd)) {
                tagNames.add(tagToAdd);
            }
        }

        mTagFlowLayout.setAdapter(new TagAdapter<String>(tagNames) {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_select, mTagFlowLayout, false);
                tv.setText(s);

                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);

                tagSelected.add(tagNames.get(position));
//                arrTab.remove(position);
//                notifyDataChanged();
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);

                tagSelected.remove(tagNames.get(position));
            }
        });


        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                final int index = position;  // get index


                return true;
            }
        });

        editTextCreateNewTag = (EditText) dialogView.findViewById(R.id.edittext_create_new_tag);

        imageButtonCreateNewTag = (Button) dialogView.findViewById(R.id.button_create_tag);
        imageButtonCreateNewTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagToCreate = editTextCreateNewTag.getText().toString().trim();
                if (tagToCreate.length() != 0) {
                    Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from " + TABLE_NAME_TAG + " where name = '" + tagToCreate + "'", null);
                    if (cursor != null && cursor.getCount() > 0) {
                        Toast.makeText(getContext(), "Tag Name already exists!", Toast.LENGTH_SHORT).show();
                    } else {

                        ContentValues questionValues = new ContentValues();
                        questionValues.put("name", tagToCreate);
                        if (questionsHelper.getWritableDatabase().insert(TABLE_NAME_TAG, null, questionValues) > 0) {
                            tagNames.add(tagToCreate);
                            mTagFlowLayout.getAdapter().notifyDataChanged();
                            editTextCreateNewTag.setText("");
                        } else {
                            Toast.makeText(getContext(), "Add tag failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });


        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        DialogSelectTagListener activity = (DialogSelectTagListener) getTargetFragment();

//                        List<Tag> result = new ArrayList<>();
//                        for (Integer key : tagSelected.keySet()) {
//                            result.add(tagSelected.get(key));
//                        }
                       activity.onTagSelected(tagSelected);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogSelectTag.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
