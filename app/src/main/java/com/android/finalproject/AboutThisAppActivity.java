package com.android.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class AboutThisAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_this_app);

        Button goBackButton = findViewById(R.id.about_this_app_go_back);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_this_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_main_page:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_add_blog:
                intent = new Intent(this, EditBlogActivity.class);
                intent.putExtra(Constants.BLOG_FOR_EDIT_PAGE, new Blog(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                startActivity(intent);
                return true;
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
