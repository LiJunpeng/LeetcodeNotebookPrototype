package com.example.louis.leetcodenotebook.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.louis.leetcodenotebook.DBHelper.QuestionsDatabaseHelper;
import com.example.louis.leetcodenotebook.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] testQuestion = new String[]{
            "Two sum",
            "2",
            "Given an array of integers, return indices of the two numbers such that they add up to a specific target.\n" +
                    "\n" +
                    "You may assume that each input would have exactly one solution, and you may not use the same element twice.\n" +
                    "\n" +
                    "Example:\n" +
                    "Given nums = [2, 7, 11, 15], target = 9,\n" +
                    "\n" +
                    "Because nums[0] + nums[1] = 2 + 7 = 9,\n" +
                    "return [0, 1].",
            "HashSet",
            "    private void initView() {\n" +
                    "        listViewQuestion = (ListView) findViewById(R.id.listview_question);\n" +
                    "        listViewQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {\n" +
                    "            @Override\n" +
                    "            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {\n" +
                    "                //System.out.println(position + \"  \" + id);\n" +
                    "\n" +
                    "                Bundle bundle = new Bundle();\n" +
                    "                bundle.putInt(\"questionID\", questionIDList.get(position));\n" +
                    "                Intent intent = new Intent(MainActivity.this, QuestionDetailActivity.class);\n" +
                    "                intent.putExtras(bundle);\n" +
                    "                startActivity(intent);\n" +
                    "            }\n" +
                    "        });\n" +
                    "        listViewQuestion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {\n" +
                    "            @Override\n" +
                    "            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {\n" +
                    "                final int index = position;\n" +
                    "                new AlertDialog.Builder(MainActivity.this)\n" +
                    "                        //  弹出窗口的最上头文字\n" +
                    "                        .setTitle(\"Delete Question\")\n" +
                    "                        //设置弹出窗口的图式\n" +
                    "                        .setIcon(android.R.drawable.ic_dialog_info)\n" +
                    "                        // 设置弹出窗口的信息\n" +
                    "                        .setMessage(\"Are you sure to delete this question?\")\n" +
                    "                        .setPositiveButton(\"Yes\",\n" +
                    "                                new DialogInterface.OnClickListener() {\n" +
                    "                                    public void onClick( DialogInterface dialoginterface, int i) {\n" +
                    "                                        // 获取位置索引\n" +
                    "                                        int questionID = questionIDList.get(index);\n" +
                    "                                        deleteQuestion(questionID);\n" +
                    "                                    }\n" +
                    "                                }\n" +
                    "                        )\n" +
                    "                        .setNegativeButton(\"No\",\n" +
                    "                                new DialogInterface.OnClickListener() {\n" +
                    "                                    public void onClick(\n" +
                    "                                            DialogInterface dialoginterface, int i) {\n" +
                    "                                        // 什么也没做\n" +
                    "\n" +
                    "                                    }\n" +
                    "                                }).show();\n" +
                    "                return true;\n" +
                    "            }\n" +
                    "\n" +
                    "        });"
    };



    final String DB_NAME_QUESTIONS = "questions";
    private QuestionsDatabaseHelper questionsHelper;
    private ListView listViewQuestion;
    ArrayList<Integer> questionIDList; //    key-value
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected  void onResume() {
        super.onResume();

        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from questions_table", null);
        inflateListView(cursor);
    }

    private void initView() {
        myToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        listViewQuestion = (ListView) findViewById(R.id.listview_question);
        listViewQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //System.out.println(position + "  " + id);

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
                        //  弹出窗口的最上头文字
                        .setTitle("Delete Question")
                        //设置弹出窗口的图式
                        .setIcon(android.R.drawable.ic_dialog_info)
                        // 设置弹出窗口的信息
                        .setMessage("Are you sure to delete this question?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick( DialogInterface dialoginterface, int i) {
                                        // 获取位置索引
                                        int questionID = questionIDList.get(index);
                                        deleteQuestion(questionID);
                                    }
                                }
                        )
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        // 什么也没做

                                    }
                                }).show();
                return true;
            }

        });


        questionsHelper = new QuestionsDatabaseHelper(getApplicationContext(), DB_NAME_QUESTIONS, 1);
        //insertQuestion();   // test
        System.out.println("====> " + DatabaseUtils.queryNumEntries(questionsHelper.getReadableDatabase(), "questions_table"));

        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from questions_table", null);
        inflateListView(cursor);
    }

    private void inflateListView(Cursor cursor) {
        questionIDList = new ArrayList<>();
        while (cursor.moveToNext()) {
            questionIDList.add(cursor.getInt(0));
        }

        SimpleCursorAdapter cursorAdaper = new SimpleCursorAdapter(getApplicationContext(), R.layout.cell_question, cursor, new String[]{"title", "level"}, new int[]{R.id.title, R.id.level}, 1);

        listViewQuestion.setAdapter(cursorAdaper);
    }

    private void insertQuestion() {

//        questionsHelper.getReadableDatabase().execSQL("insert into questions_table values(null, ?, ?, ?, ?, ?)", new String[]{"two sum", "2", "2 to target", "hint?", "fffffff"});
        questionsHelper.getReadableDatabase().execSQL("insert into questions_table values(null, ?, ?, ?, ?, ?)", testQuestion);

        Cursor cursor = questionsHelper.getReadableDatabase().rawQuery("select * from questions_table", null);
        inflateListView(cursor);           //刷新listview
    }


    private boolean deleteQuestion(int _id) {
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(_id) };
        try{
            questionsHelper.getReadableDatabase().delete("questions_table", whereClause,whereArgs);
            Cursor cursor = questionsHelper.getWritableDatabase().rawQuery("select * from questions_table", null);
            inflateListView(cursor);
        }catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "删除数据库失败",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startAdding() {
        Intent add = new Intent(MainActivity.this, AddQuestionActivity.class);
        startActivity(add);
    }

    private void startFiltering() {

    }

}
