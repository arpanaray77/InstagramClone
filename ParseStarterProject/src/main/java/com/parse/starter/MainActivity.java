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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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


public class MainActivity extends AppCompatActivity implements OnClickListener,View.OnKeyListener {

    Boolean signupModeActive = true;
    TextView login;
    EditText username;
    EditText password;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN)
        {
            signup(view);
        }
        return false;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {
            Log.i("Switch to", "Login was tapped");
            Button signUp = (Button) findViewById(R.id.signbutton);
            if (signupModeActive) {
                signupModeActive = false;
                signUp.setText("Login");
                login.setText("Not yet Signed up? Signup");
            }
            //response is not invoked on clicking other parts of screen
            else if (view.getId() == R.id.imageView || view.getId() == R.id.background) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            else {
                signupModeActive = true;
                signUp.setText("Sign Up");
                login.setText("Already Signed Up? Login");
            }
        }
    }

    public void showUser() {
        Intent intent=new  Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        ImageView logoo=(ImageView)findViewById(R.id.imageView);
        RelativeLayout background=(RelativeLayout)findViewById(R.id.background);
        logoo.setOnKeyListener(this);
        background.setOnKeyListener(this);
        password.setOnKeyListener(this);

        //if logged in go to login page
        if(ParseUser.getCurrentUser()!=null)
        {
            showUser();
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signup(View view) {

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
                            showUser();
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
                            showUser();
                           // Toast.makeText(MainActivity.this, "Username or Password incorrect", LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, "Username or Password incorrect", LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}