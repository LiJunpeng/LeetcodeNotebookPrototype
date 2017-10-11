package com.example.louis.leetcodenotebook.activities.AddQuestionFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ActionMenuItemView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.DataClass.Tag;
import com.example.louis.leetcodenotebook.R;
import com.example.louis.leetcodenotebook.activities.AddQuestionActivity;
import com.example.louis.leetcodenotebook.activities.Dialog.DialogSelectTag;
import com.example.louis.leetcodenotebook.activities.Dialog.DialogSelectTagListener;
import com.example.louis.leetcodenotebook.activities.MainActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Louis on 10/8/2017.
 */

public class FragmentManul extends Fragment implements DialogSelectTagListener {

    private EditText editTextTitle;
    private EditText editTextHint;
    private EditText editTextDescription;
    private EditText editTextAnswer;
    private RadioGroup radioGroupLevel;
    private TagFlowLayout mFlowLayout;

    private ActionMenuItemView buttonSaveQuestion;

    private int level = 1;
    private List<String> tagNames = new ArrayList<>();
    private List<Tag> tagList = new ArrayList<>();

    final String DB_NAME_QUESTIONS = "questions";
    final String TABLE_NAME_TAG = "tag_table";
    final String TABLE_NAME_QUESTION_TAG = "question_tag_table";
    final String TABLE_NAME_QUESTIONS = "questions_table";
    private QuestionsDatabaseHelper questionsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_manul, container, false);
        editTextTitle = (EditText) view.findViewById(R.id.edittext_add_question_title);
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    buttonSaveQuestion.setEnabled(true);
                } else {
                    buttonSaveQuestion.setEnabled(false);
                }
            }
        });

        radioGroupLevel = (RadioGroup) view.findViewById(R.id.radio_group_level);
        radioGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_level_easy:
                        level = 1;
                        break;
                    case R.id.radio_level_medium:
                        level = 2;
                        break;
                    case R.id.radio_level_hard:
                        level = 3;
                        break;
                    default:
                        break;
                }
                System.out.println("Select: " + level);
            }
        });

        editTextHint = (EditText) view.findViewById(R.id.edittext_add_question_hint);
        editTextDescription = (EditText) view.findViewById(R.id.edittext_add_question_description);
        editTextAnswer = (EditText) view.findViewById(R.id.edittext_add_question_answer);


//        questionsHelper = new QuestionsDatabaseHelper(getActivity(), DB_NAME_QUESTIONS, 1);
//        System.out.println("====> " + DatabaseUtils.queryNumEntries(questionsHelper.getReadableDatabase(), "tag_table"));
//        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from " + TABLE_NAME_TAG, null);
//        while (cursor.moveToNext()) {
//            tagList.add(new Tag(cursor.getInt(0), cursor.getString(1)));
//        }
//        for (Tag t : tagList) {
//            tagNames.add(t.getName());
//        }
        tagNames.add("+");

        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout_edit_tag);
//        tagList.add("test");
//        tagList.add("+");
        final LayoutInflater mInflater = LayoutInflater.from(view.getContext());
        mFlowLayout.setAdapter(new TagAdapter<String>(tagNames) {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv;
                if (position == tagNames.size() - 1) {   // render 'Add tag'
                    tv = (TextView) mInflater.inflate(R.layout.tag_add, mFlowLayout, false);
                } else {
                    tv = (TextView) mInflater.inflate(R.layout.tag_edit, mFlowLayout, false);
                }
                tv.setText(s);

                return tv;
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                final int index = position;  // get index

                if (position == tagNames.size() - 1) {   // add tag

                    DialogFragment selectTag = new DialogSelectTag();
                    selectTag.setTargetFragment(FragmentManul.this, 0);
                    Bundle args = new Bundle();
                    ArrayList<String> tagToAvoid = new ArrayList<String>();
                    for (String tag : tagNames) {
                        tagToAvoid.add(tag);
                    }
                    args.putStringArrayList("tagToAvoid", tagToAvoid);
                    selectTag.setArguments(args);
                    selectTag.show(getFragmentManager(), "selectTag");

                    //System.out.println("=========> add");
                } else {    // delete tag
                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete Tag")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("Are you sure to delete this tag from this question?")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick( DialogInterface dialoginterface, int i) {
                                            tagNames.remove(index);
                                            mFlowLayout.getAdapter().notifyDataChanged();
                                        }
                                    }
                            )
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialoginterface, int i) {

                                        }
                                    }).show();
                }

                return true;
            }
        });

        return view;
    }

    public void setButton(ActionMenuItemView button) {
        this.buttonSaveQuestion = button;
    }

    public Map<String, String> getInput() {
        Map<String, String> input = new HashMap<>();
        input.put("title", editTextTitle.getText().toString());
        input.put("level", String.valueOf(level));
        input.put("hint", editTextHint.getText().toString());
        input.put("description", editTextDescription.getText().toString());
        input.put("answer", editTextAnswer.getText().toString());

        return input;
    }

    @Override
    public void onTagSelected(Set<String> tagSeleted) {
        for (String tagName : tagSeleted) {
            if (!tagNames.contains(tagName)) {
                tagNames.add(tagNames.size() - 1, tagName);
            }
        }

        mFlowLayout.getAdapter().notifyDataChanged();
    }


}
