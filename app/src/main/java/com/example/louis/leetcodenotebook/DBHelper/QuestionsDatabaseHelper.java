package com.example.louis.leetcodenotebook.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Louis on 10/6/2017.
 */

public class QuestionsDatabaseHelper extends SQLiteOpenHelper {
    final String SQL_CREATE_QUESTION_TABLE = "create table questions_table (" +

            "_id integer primary key autoincrement, " +

            "title varchar(50), " +

            "level integer, " +

            "description varchar(1000), " +

            "hint varchar(200), " +

            "answer varchar(5000))";

    final  String SQL_CREATE_TAG_TABLE = "create table tag_table (" +

            "_id integer primary key autoincrement, " +

            "name varchar(50))";

    final String SQL_CREATE_QUESTION_TAG_TABLE = "create table question_tag_table (" +

            "_id integer primary key autoincrement, " +

            "question_id integer, " +

            "tag_id integer)";

    final String[] SAMPLE_QUESTION = new String[]{
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
                    "        });"};


    public QuestionsDatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_TAG_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TAG_TABLE);


//        db.execSQL(this.getReadableDatabase().execSQL("insert into questions_table values(null, ?, ?, ?, ?, ?)", SAMPLE_QUESTION));
//        this.getReadableDatabase().execSQL("insert into questions_table values(null, ?, ?, ?, ?, ?)", SAMPLE_QUESTION);
        db.execSQL("insert into questions_table (title, level, description, hint, answer) values('Two sum', 1, 'descr', 'h', 'answer')");
        db.execSQL("insert into tag_table (name) values('Stack')");
        db.execSQL("insert into tag_table (name) values('Queue')");
        db.execSQL("insert into tag_table (name) values('Two Pointer')");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("call update");
    }
}
