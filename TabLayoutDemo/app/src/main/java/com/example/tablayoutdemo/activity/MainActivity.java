package com.example.tablayoutdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.tablayoutdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                goToTabLayout(OneActivity.class);
                break;
            case R.id.btn2:
                goToTabLayout(TwoActivity.class);
                break;
        }
    }

    private void goToTabLayout(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
