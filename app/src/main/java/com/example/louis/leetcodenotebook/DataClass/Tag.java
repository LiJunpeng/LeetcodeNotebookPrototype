package com.example.louis.leetcodenotebook.DataClass;

/**
 * Created by Louis on 10/10/2017.
 */

public class Tag {
    private String name;
    private int id;
    public Tag(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
