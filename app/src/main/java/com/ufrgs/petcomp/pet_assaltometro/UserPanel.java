package com.ufrgs.petcomp.pet_assaltometro;

/**
 * Created by GuiPC on 20/12/2016.
 */

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class UserPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assal_user);

        Intent intent = getIntent();
        String username = intent.getStringExtra(Login.USER_NAME);

        TextView textView = (TextView) findViewById(R.id.userTextView);

        textView.setText("Welcome " + username);
        }
}