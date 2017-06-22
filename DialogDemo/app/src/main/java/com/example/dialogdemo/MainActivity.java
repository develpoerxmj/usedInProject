package com.example.dialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.show(MainActivity.this, new MyDialog.Click() {
                    @Override
                    public void click(String string) {
                        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
