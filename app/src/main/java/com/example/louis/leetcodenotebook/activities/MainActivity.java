package com.example.louis.leetcodenotebook.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int FILTER_SETTING_REQUEST = 1;

    private final String DB_NAME_QUESTIONS = "questions";
    private final String TABLE_NAME_QUESTIONS = "questions_table";
    private final String TABLE_NAME_TAG = "tag_table";
    private final String TABLE_NAME_QUESTION_TAG = "question_tag_table";
    private QuestionsDatabaseHelper questionsHelper;

    private EditText editTextSearchTitle;
    private ListView listViewQuestion;
    private ArrayList<Integer> questionIDList = new ArrayList<>(); //    index - question_id
    private Toolbar myToolbar;

    private ArrayList<String> tagsFilter = new ArrayList<>();
    private ArrayList<Integer> levelsFilter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected  void onResume() {
        super.onResume();

        if (editTextSearchTitle.getText() != null && editTextSearchTitle.getText().toString().length() != 0) {
            inflateListView(searchTitle(editTextSearchTitle.getText().toString()));
        } else {
            inflateListView(getFiltered());
        }
    }

    private void initView() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        editTextSearchTitle = (EditText) findViewById(R.id.edittext_search_title);
        editTextSearchTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (editTextSearchTitle.getText() != null && editTextSearchTitle.getText().toString().length() != 0) {
                        inflateListView(searchTitle(editTextSearchTitle.getText().toString()));
                        System.out.println(" ===========> search: " + editTextSearchTitle.getText().toString());
                    } else {
                        inflateListView(getFiltered());
                    }

                    editTextSearchTitle.clearFocus();
                    InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editTextSearchTitle.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        listViewQuestion = (ListView) findViewById(R.id.listview_question);
        listViewQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //System.out.println(position + "  " + id);
//                System.out.println("ffff: " + questionIDList.size());
                Bundle bundle = new Bundle();
                bundle.putInt("questionID", questionIDList.get(position));
                Intent intent = new Intent(MainActivity.this, QuestionDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listViewQuestion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int index = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Question")   // Set title
                        .setIcon(android.R.drawable.ic_dialog_info)   // Set window Icon
                        .setMessage("Are you sure to delete this question?")  // Set message
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick( DialogInterface dialoginterface, int i) {
                                        int questionID = questionIDList.get(index);    // get question id from clicked index
                                        deleteQuestion(questionID);
                                    }
                                }
                        )
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {}     // dismiss
                                }).show();
                return true;
            }

        });

        questionsHelper = new QuestionsDatabaseHelper(getApplicationContext(), DB_NAME_QUESTIONS, 1);   // init db helper
    }

    private void inflateListView(Cursor cursor) {
        questionIDList.clear();
        while (cursor.moveToNext()) {
            questionIDList.add(cursor.getInt(0));  // create index - id mapping for listview
        }

        SimpleCursorAdapter cursorAdaper = new SimpleCursorAdapter(getApplicationContext(), R.layout.cell_question, cursor, new String[]{"title", "level"}, new int[]{R.id.title, R.id.level_icon}, 1);  // create new adapter
        cursorAdaper.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.level_icon) {
                    ImageView iconImageView = (ImageView) view;
                    int level = cursor.getInt(2);
                    if (level == 1) {
                        iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_level_easy));
                    } else if (level == 2) {
                        iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_level_medium));
                    } else if (level == 3) {
                        iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_level_hard));
                    }
                    return true;
                } else {  // Process the rest of the adapter with default settings.
                    return false;
                }
            }
        });

        listViewQuestion.setAdapter(cursorAdaper);  // update listview basing on filter setting
    }

    private boolean deleteQuestion(int _id) {
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(_id) };
        try{
            questionsHelper.getReadableDatabase().delete("questions_table", whereClause, whereArgs);
            Cursor cursor = questionsHelper.getWritableDatabase().rawQuery("select * from questions_table", null);
            inflateListView(cursor);
        }catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Failed to delete",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        myToolbar.setTitle("LeetcodeNotebook");
      //  myToolbar.setLogo(getResources().getDrawable(R.drawable.ic_menu_logo));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.iconPrimary));
        myToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_logo)); // to do
        myToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_menu_overflow));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
                startAdding();
                return true;
            case R.id.filter:
                startFiltering();
                return true;
            case R.id.info:
                startAppInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startAdding() {
        Intent add = new Intent(MainActivity.this, AddQuestionActivity.class);
        startActivity(add);
    }

    private void startFiltering() {
        Intent filter = new Intent(MainActivity.this, FilterSettingActivity.class);
        startActivityForResult(filter, FILTER_SETTING_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FILTER_SETTING_REQUEST) : {
                if (resultCode == Activity.RESULT_OK) {
                    tagsFilter = data.getStringArrayListExtra("tags");     // update filter
                    levelsFilter = data.getIntegerArrayListExtra("level");  // update filter
                }
                break;
            }
        }
    }

    private Cursor getFiltered () {
        ArrayList<Integer> tagId = new ArrayList<>();
        for (String tag : tagsFilter) {
            Cursor id =questionsHelper.getReadableDatabase().query(TABLE_NAME_TAG, new String[]{"_id", "name"}, "name = ?", new String[]{tag}, null, null,null, null);
            if (id != null && id.getCount() > 0) {
                id.moveToFirst();
                tagId.add(id.getInt(0));    // Convert tag names to tag id and save to tagId
            }
        }

        String rawQuery = "SELECT * FROM " + TABLE_NAME_QUESTIONS;    // Construct query statement
        StringBuilder levelFilter = new StringBuilder();
        if (levelsFilter != null && levelsFilter.size() != 0) {     // Build level query condition, if no level selected use all levels as default
            levelFilter.append(TABLE_NAME_QUESTIONS + ".level = " + levelsFilter.get(0));
            for (int i = 1; i < levelsFilter.size(); i++) {
                levelFilter.append(" OR " + TABLE_NAME_QUESTIONS + ".level = " + levelsFilter.get(i));
            }
        }

        StringBuilder tagFilter = new StringBuilder();
        if (tagsFilter == null || tagsFilter.size() == 0) {    // Build tag query condition
            if (levelFilter.length() != 0) {      // If no tag selected, use all tags as default
                rawQuery += " WHERE " + levelFilter.toString();
            }
        } else {
            tagFilter.append(" ( tag_id = " + tagId.get(0 ) + " ");
            for (int i = 1; i < tagId.size(); i++) {
                tagFilter.append(" OR tag_id = " + tagId.get(i));       // Add tags
            }
            tagFilter.append(" ) ");

            if (levelFilter.length() != 0) {
                tagFilter.append(" AND (" + levelFilter.toString() + ") ");   // Add level
            }
            tagFilter.append(" GROUP BY " + TABLE_NAME_QUESTIONS + "._id");
            rawQuery += " INNER JOIN " + TABLE_NAME_QUESTION_TAG + " ON " + TABLE_NAME_QUESTIONS + "._id " + " = " + TABLE_NAME_QUESTION_TAG + ".question_id WHERE " + tagFilter.toString();
        }

        Cursor filteredCursor = questionsHelper.getReadableDatabase().rawQuery(rawQuery, null);

        return filteredCursor;
    }

    private Cursor searchTitle(String titleToSearch) {
        ArrayList<Integer> tagId = new ArrayList<>();
        for (String tag : tagsFilter) {
            Cursor id =questionsHelper.getReadableDatabase().query(TABLE_NAME_TAG, new String[]{"_id", "name"}, "name = ?", new String[]{tag}, null, null,null, null);
            if (id != null && id.getCount() > 0) {
                id.moveToFirst();
                tagId.add(id.getInt(0));    // Convert tag names to tag id and save to tagId
            }
        }

        String rawQuery = "SELECT * FROM " + TABLE_NAME_QUESTIONS;    // Construct query statement
        StringBuilder levelFilter = new StringBuilder();
        if (levelsFilter != null && levelsFilter.size() != 0) {     // Build level query condition, if no level selected use all levels as default
            levelFilter.append(TABLE_NAME_QUESTIONS + ".level = " + levelsFilter.get(0));
            for (int i = 1; i < levelsFilter.size(); i++) {
                levelFilter.append(" OR " + TABLE_NAME_QUESTIONS + ".level = " + levelsFilter.get(i));
            }
        }

        StringBuilder tagFilter = new StringBuilder();
        if (tagsFilter == null || tagsFilter.size() == 0) {    // Build tag query condition
            if (levelFilter.length() != 0) {      // If no tag selected, use all tags as default
                rawQuery += " WHERE ( " + levelFilter.toString() + " ) AND " + TABLE_NAME_QUESTIONS + ".title like '%" + titleToSearch + "%'";
            } else {
                rawQuery += " WHERE " + TABLE_NAME_QUESTIONS + ".title like '%" + titleToSearch + "%'";
            }
        } else {
            tagFilter.append(" ( tag_id = " + tagId.get(0 ) + " ");
            for (int i = 1; i < tagId.size(); i++) {
                tagFilter.append(" OR tag_id = " + tagId.get(i));       // Add tags
            }
            tagFilter.append(" ) ");

            if (levelFilter.length() != 0) {
                tagFilter.append(" AND (" + levelFilter.toString() + ") ");   // Add level
            }
            tagFilter.append(" AND " + TABLE_NAME_QUESTIONS + ".title like '%" + titleToSearch + "%' ");
            tagFilter.append(" GROUP BY " + TABLE_NAME_QUESTIONS + "._id");
            rawQuery += " INNER JOIN " + TABLE_NAME_QUESTION_TAG + " ON " + TABLE_NAME_QUESTIONS + "._id " + " = " + TABLE_NAME_QUESTION_TAG + ".question_id WHERE " + tagFilter.toString();

        }

        //rawQuery += " AND " + TABLE_NAME_QUESTIONS + ".title like '%" + titleToSearch + "%'";

        Cursor filteredCursor = questionsHelper.getReadableDatabase().rawQuery(rawQuery, null);

        return filteredCursor;
    }


    private void startAppInfo() {
        Intent appInfo = new Intent(MainActivity.this, AppInfoActivity.class);
        startActivity(appInfo);
    }
}
