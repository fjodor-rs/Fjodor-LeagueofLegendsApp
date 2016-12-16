package com.fjodor.fjodor_leagueoflegendsapp;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * This activity was made using the Firebase Quickstart guide for Android:
 *
 *      https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java#L45-L45
 *
 * The activity implements the progressbar, toolbar and NavigationView for
 * the EmailPasswordActivity.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener (toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




    }

    /**
     * Makes the menu options in the NavigationView clickable, and implements what
     * happens when they are clicked.
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.isChecked()){
            item.setChecked(false);

        }
        else{
            item.setChecked(true);
        }

        drawerLayout.closeDrawers();

        switch (item.getItemId()){
            case R.id.main_menu:
                Intent main_intent = new Intent(getApplicationContext(), EmailPasswordActivity.class);
                startActivity(main_intent);
                finish();
                return true;
            case R.id.search:
                Intent search_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(search_intent);
                finish();
                return true;
            case R.id.stats :
                Toast.makeText(getApplicationContext(), "Stats", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
}