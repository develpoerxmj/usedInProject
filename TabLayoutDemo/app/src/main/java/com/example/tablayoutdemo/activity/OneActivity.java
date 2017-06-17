package com.example.tablayoutdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tablayoutdemo.R;
import com.example.tablayoutdemo.fragment.TabLayoutFragment;

public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout, new TabLayoutFragment())
                .commit();
    }
}
