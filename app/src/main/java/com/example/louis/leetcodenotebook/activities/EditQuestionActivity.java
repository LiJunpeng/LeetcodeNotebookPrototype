package com.example.louis.leetcodenotebook.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;
import com.example.louis.leetcodenotebook.activities.AddQuestionFragments.FragmentManul;
import com.example.louis.leetcodenotebook.activities.Dialog.DialogSelectTag;
import com.example.louis.leetcodenotebook.activities.Dialog.DialogSelectTagFromEditActivity;
import com.example.louis.leetcodenotebook.activities.Dialog.DialogSelectTagListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Louis on 10/19/2017.
 */

public class EditQuestionActivity extends AppCompatActivity implements DialogSelectTagListener {

    final String DB_NAME_QUESTIONS = "questions";
    final String TABLE_NAME_TAG = "tag_table";
    final String TABLE_NAME_QUESTION_TAG = "question_tag_table";
    final String TABLE_NAME_QUESTIONS = "questions_table";
    private QuestionsDatabaseHelper questionsHelper;

    private ImageButton imageButtonBack;
    private ImageButton imageButtonSave;
    private EditText editTextTitle;
    private EditText editTextHint;
    private EditText editTextDescription;
    private EditText editTextNote;
    private EditText editTextAnswer;
    private RadioGroup radioGroupLevel;
    private TagFlowLayout mFlowLayout;

    private List<String> tagNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        initView();
        initContent();
    }

    private void initView() {
        imageButtonBack = (ImageButton) findViewById(R.id.toolbar_edit_back_button);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo

                setResult(Activity.RESULT_OK, null);
                finish();
            }
        });

        imageButtonSave = (ImageButton) findViewById(R.id.toolbar_edit_save_button);
        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // // TODO:
            }
        });

        editTextTitle = (EditText) findViewById(R.id.edittext_edit_question_title);
        editTextHint = (EditText) findViewById(R.id.edittext_edit_question_hint);
        editTextDescription = (EditText) findViewById(R.id.edittext_edit_question_description);
        editTextNote = (EditText) findViewById(R.id.edittext_edit_question_note);
        editTextAnswer = (EditText) findViewById(R.id.edittext_edit_question_answer);

        tagNames.add("+");
        radioGroupLevel = (RadioGroup) findViewById(R.id.edit_question_radio_group_level);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.flowlayout_edit_quesiton_tag);
        final LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
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

                    DialogFragment selectTag = new DialogSelectTagFromEditActivity();    // Create dialog
                    Bundle args = new Bundle();
                    ArrayList<String> tagToAvoid = new ArrayList<String>();
                    for (String tag : tagNames) {
                        tagToAvoid.add(tag);
                    }
                    args.putStringArrayList("tagToAvoid", tagToAvoid);  // Pass tag names that already selected
                    selectTag.setArguments(args);
                    selectTag.show(getSupportFragmentManager(), "selectTag");

                } else {    // delete tag
                    new AlertDialog.Builder(EditQuestionActivity.this)
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
    }

    private void initContent() {

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
