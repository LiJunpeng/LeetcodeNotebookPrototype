package com.example.louis.leetcodenotebook.activities;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.louis.leetcodenotebook.R;

/**
 * Created by Louis on 10/18/2017.
 */

public class AppInfoActivity extends AppCompatActivity {

    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        initView();
    }

    public void initView() {
        imageButtonBack = (ImageButton) findViewById(R.id.about_toolbar_back_button);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
