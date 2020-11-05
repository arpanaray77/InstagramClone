/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    Boolean signupModeActive = true;
    TextView login;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {
            Log.i("Switch to", "Login was tapped");
            Button signUp = (Button) findViewById(R.id.signbutton);
            if (signupModeActive) {
                signupModeActive = false;
                signUp.setText("Login");
                login.setText("Not yet Signed up? Signup");
            } else {
                signupModeActive = true;
                signUp.setText("Sign Up");
                login.setText("Already Signed Up? Login");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    public void signup(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        if (username.getText().toString().matches("") || password.getText().toString().matches(""))
            Toast.makeText(this, "Username and password are required", LENGTH_SHORT).show();
        else {
            if (signupModeActive) {
                // creating a parseuser
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signup Done", "Yes");
                        } else
                            Toast.makeText(MainActivity.this, e.getMessage(), LENGTH_SHORT).show();
                    }
                });
            } else {
                //login
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("Login", "Logged In");
                           // Toast.makeText(MainActivity.this, "Username or Password incorrect", LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, "Username or Password incorrect", LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}