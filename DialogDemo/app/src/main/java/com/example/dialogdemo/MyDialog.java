package com.example.dialogdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sz132 on 2017/6/22.
 */

public class MyDialog {

    private static AlertDialog alertDialog;
    private static Button button;
    private static EditText text;

    public static void show(Context context, final Click click){
        alertDialog = new AlertDialog.Builder(context, R.style.Dialog_NO_TITLE).create();
        alertDialog.show();
        alertDialog.getWindow().setContentView(R.layout.dialog);

        button = (Button) alertDialog.getWindow().findViewById(R.id.button);
        text = (EditText) alertDialog.getWindow().findViewById(R.id.et);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.click(text.getText().toString());
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                alertDialog = null;
            }
        });
    }

    interface Click{
        void click(String string);
    }
}
