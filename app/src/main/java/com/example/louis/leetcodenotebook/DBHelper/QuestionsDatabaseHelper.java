package com.example.louis.leetcodenotebook.DBHelper;

import android.content.Context;
import android.database.Cursor;
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

            "answer varchar(5000), " +

            "note varchar(2000))";

    final  String SQL_CREATE_TAG_TABLE = "create table tag_table (" +

            "_id integer primary key autoincrement, " +

            "name varchar(50))";

    final String SQL_CREATE_QUESTION_TAG_TABLE = "create table question_tag_table (" +

            "_id integer primary key autoincrement, " +

            "question_id integer, " +

            "tag_id integer)";

    final String[] SAMPLE_QUESTION_ONE = new String[]{
            "Two sum",
            "1",
            "Given an array of integers, return indices of the two numbers such that they add up to a specific target.\n" +
                    "\n" +
                    "You may assume that each input would have exactly one solution, and you may not use the same element twice.\n" +
                    "\n" +
                    "Example:\n" +
                    "Given nums = [2, 7, 11, 15], target = 9,\n" +
                    "\n" +
                    "Because nums[0] + nums[1] = 2 + 7 = 9,\n" +
                    "return [0, 1].",
            "Use Hash Table to cache",
            "public class Solution {\n" +
                    "    public int[] twoSum(int[] nums, int target) {\n" +
                    "        \n" +
                    "        if(nums == null || nums.length < 2) {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        \n" +
                    "        HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();\n" +
                    "        int[] result = new int[2];\n" +
                    "        h.put(target - nums[0], 0);\n" +
                    "        \n" +
                    "        for(int i = 1; i < nums.length; i++){\n" +
                    "            if(h.containsKey(nums[i])) {\n" +
                    "                result[0] = h.get(nums[i]);\n" +
                    "                result[1] = i;\n" +
                    "                break;\n" +
                    "            } else {\n" +
                    "                h.put(target - nums[i], i);\n" +
                    "            }\n" +
                    "        }\n" +
                    "        return result;\n" +
                    "    }\n" +
                    "}",
            "Test note"
    };

    final String[] SAMPLE_QUESTION_TWO = new String[]{
            "Longest Substring Without Repeating Characters",
            "2",
            "Given a string, find the length of the longest substring without repeating characters.\n" +
                    "\n" +
                    "Examples:\n" +
                    "\n" +
                    "Given \"abcabcbb\", the answer is \"abc\", which the length is 3.\n" +
                    "\n" +
                    "Given \"bbbbb\", the answer is \"b\", with the length of 1.\n" +
                    "\n" +
                    "Given \"pwwkew\", the answer is \"wke\", with the length of 3. Note that the answer must be a substring, \"pwke\" is a subsequence and not a substring.",
            "Use two pointer to test possible substring.",
            "public class Solution {\n" +
                    "    public int lengthOfLongestSubstring(String s) {\n" +
                    "        \n" +
                    "        if(s == null || s.length() == 0) {\n" +
                    "            return 0;\n" +
                    "        }\n" +
                    "        \n" +
                    "        int[] map = new int[128];\n" +
                    "        int left = 0;\n" +
                    "        int right = 0;\n" +
                    "        int max = 0;\n" +
                    "        char[] str = s.toCharArray();\n" +
                    "        \n" +
                    "        while(right < s.length()) {\n" +
                    "            \n" +
                    "            while(map[str[right]] > 0) {\n" +
                    "                map[str[left++]]--;\n" +
                    "            }\n" +
                    "            \n" +
                    "            max = Math.max(max, right - left + 1);\n" +
                    "        \n" +
                    "            map[str[right++]]++;\n" +
                    "        }\n" +
                    "        \n" +
                    "        return max;\n" +
                    "    }\n" +
                    "}\n" +
                    "       ",
            "Using two pointer to indicate the subarray and a \"max\" varaible to keep tracking the global max value."
    };


    final String[] SAMPLE_QUESTION_THREE = new String[]{
            "Median of Two Sorted Arrays",
            "3",
            "There are two sorted arrays nums1 and nums2 of size m and n respectively.\n" +
                    "\n" +
                    "Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).\n" +
                    "\n" +
                    "Example 1:\n" +
                    "nums1 = [1, 3]\n" +
                    "nums2 = [2]\n" +
                    "\n" +
                    "The median is 2.0\n" +
                    "Example 2:\n" +
                    "nums1 = [1, 2]\n" +
                    "nums2 = [3, 4]\n" +
                    "\n" +
                    "The median is (2 + 3)/2 = 2.5",
            "Pick k / 2 elements from one array each time",
            "public class Solution {\n" +
                    "    public double findMedianSortedArrays(int[] nums1, int[] nums2) {\n" +
                    "        int sum = nums1.length + nums2.length;\n" +
                    "        if (sum % 2 == 0) {\n" +
                    "            return (double)(helper(nums1, 0, nums2, 0, sum / 2) + helper(nums1, 0, nums2, 0, sum / 2 + 1)) / 2;\n" +
                    "        } else {\n" +
                    "            return helper(nums1, 0, nums2, 0, sum / 2 + 1);\n" +
                    "        }\n" +
                    "    }\n" +
                    "    \n" +
                    "    private int helper(int[] nums1, int index1, int[] nums2, int index2, int k) {\n" +
                    "        if (nums1.length == index1) {\n" +
                    "            return nums2[index2 + k - 1];\n" +
                    "        }\n" +
                    "        if (nums2.length == index2) {\n" +
                    "            return nums1[index1 + k - 1];\n" +
                    "        }\n" +
                    "        \n" +
                    "        if (k == 1) {\n" +
                    "            return Math.min(nums1[index1], nums2[index2]);\n" +
                    "        }\n" +
                    "        \n" +
                    "        if (index1 + k / 2  > nums1.length) {\n" +
                    "            return helper(nums1, index1, nums2, index2 + k / 2, k - k / 2);\n" +
                    "        }\n" +
                    "        if (index2 + k / 2 > nums2.length) {\n" +
                    "            return helper(nums1, index1 + k / 2, nums2, index2, k - k / 2);\n" +
                    "        }\n" +
                    "        \n" +
                    "        if (nums1[index1 + k / 2 - 1] < nums2[index2 + k / 2 - 1]) {\n" +
                    "            return helper(nums1, index1 + k / 2, nums2, index2, k - k / 2);\n" +
                    "        } else {\n" +
                    "            return helper(nums1, index1, nums2, index2 + k / 2, k - k / 2);\n" +
                    "        }\n" +
                    "    }   \n" +
                    "}",
            "Test test"
    };


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
//        db.execSQL("insert into questions_table (title, level, description, hint, answer) values('Two sum', 1, 'descr', 'h', 'answer')");
        db.execSQL("insert into questions_table (title, level, description, hint, answer, note) values('"+ SAMPLE_QUESTION_ONE[0] +"', " + Integer.valueOf(SAMPLE_QUESTION_ONE[1]) + ", '" + SAMPLE_QUESTION_ONE[2] + "', '" + SAMPLE_QUESTION_ONE[3] +"', '" + SAMPLE_QUESTION_ONE[4] +"','" + SAMPLE_QUESTION_ONE[5] + "')");
        db.execSQL("insert into questions_table (title, level, description, hint, answer, note) values('"+ SAMPLE_QUESTION_TWO[0] +"', " + Integer.valueOf(SAMPLE_QUESTION_TWO[1]) + ", '" + SAMPLE_QUESTION_TWO[2] + "', '" + SAMPLE_QUESTION_TWO[3] +"', '" + SAMPLE_QUESTION_TWO[4] +"','" + SAMPLE_QUESTION_TWO[5] + "')");
        db.execSQL("insert into questions_table (title, level, description, hint, answer, note) values('"+ SAMPLE_QUESTION_THREE[0] +"', " + Integer.valueOf(SAMPLE_QUESTION_THREE[1]) + ", '" + SAMPLE_QUESTION_THREE[2] + "', '" + SAMPLE_QUESTION_THREE[3] +"', '" + SAMPLE_QUESTION_THREE[4] +"', '" + SAMPLE_QUESTION_THREE[5] + "')");
        db.execSQL("insert into tag_table (name) values('Array')");
        db.execSQL("insert into tag_table (name) values('String')");
        db.execSQL("insert into tag_table (name) values('Tree')");
        db.execSQL("insert into tag_table (name) values('Linked List')");
        db.execSQL("insert into tag_table (name) values('Breadth-first Search')");
        db.execSQL("insert into tag_table (name) values('Depth-first Search')");
        db.execSQL("insert into tag_table (name) values('Hash Table')");
        db.execSQL("insert into tag_table (name) values('Stack')");
        db.execSQL("insert into tag_table (name) values('Queue')");
        db.execSQL("insert into tag_table (name) values('Two Pointers')");
        db.execSQL("insert into tag_table (name) values('Binary Search')");
        db.execSQL("insert into tag_table (name) values('Divide and Conquer')");
        db.execSQL("insert into tag_table (name) values('Dynamic Programming')");
        db.execSQL("insert into tag_table (name) values('Greedy')");
        db.execSQL("insert into tag_table (name) values('Backtracking')");

        Cursor twoSum = db.rawQuery("select * from questions_table where title = ?", new String[]{SAMPLE_QUESTION_ONE[0]});
        twoSum.moveToFirst();

        Cursor longestSubstring = db.rawQuery("select * from questions_table where title = ?", new String[]{SAMPLE_QUESTION_TWO[0]});
        longestSubstring.moveToFirst();

        Cursor mediamOfTwoArrays = db.rawQuery("select * from questions_table where title = ?", new String[]{SAMPLE_QUESTION_THREE[0]});
        mediamOfTwoArrays.moveToFirst();

        Cursor arrayCursor = db.rawQuery("select * from tag_table where name = ?", new String[]{"Array"});
        arrayCursor.moveToFirst();

        Cursor hashtableCursor = db.rawQuery("select * from tag_table where name = ?", new String[]{"Hash Table"});
        hashtableCursor.moveToFirst();

        Cursor twopointersCursor = db.rawQuery("select * from tag_table where name = ?", new String[]{"Two Pointers"});
        twopointersCursor.moveToFirst();

        Cursor stringCursor = db.rawQuery("select * from tag_table where name = ?", new String[]{"String"});
        stringCursor.moveToFirst();

        Cursor binaryCursor = db.rawQuery("select * from tag_table where name = ?", new String[]{"Binary Search"});
        binaryCursor.moveToFirst();

        Cursor dqCursor = db.rawQuery("select * from tag_table where name = ?", new String[]{"Divide and Conquer"});
        dqCursor.moveToFirst();


        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + twoSum.getInt(0) + ", " + arrayCursor.getInt(0) +")");
        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + twoSum.getInt(0) + ", " + hashtableCursor.getInt(0) +")");

        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + longestSubstring.getInt(0) + ", " + hashtableCursor.getInt(0) +")");
        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + longestSubstring.getInt(0) + ", " + twopointersCursor.getInt(0) +")");
        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + longestSubstring.getInt(0) + ", " + stringCursor.getInt(0) +")");

        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + mediamOfTwoArrays.getInt(0) + ", " + arrayCursor.getInt(0) +")");
        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + mediamOfTwoArrays.getInt(0) + ", " + binaryCursor.getInt(0) +")");
        db.execSQL("insert into question_tag_table (question_id, tag_id) values(" + mediamOfTwoArrays.getInt(0) + ", " + dqCursor.getInt(0) +")");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("call update");
    }
}
