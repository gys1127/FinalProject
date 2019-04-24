package com.android.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditBlogActivity extends AppCompatActivity {

    private EditText titleEditText;
    private ImageButton selectImageButton;
    private EditText bodyEditText;
    private CheckBox publicBlog;
    private Button saveButton;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);

        titleEditText = findViewById(R.id.blog_edit_page_title_edit_text);
        selectImageButton = findViewById(R.id.blog_edit_page_image_button);
        bodyEditText = findViewById(R.id.blog_edit_page_body_edit_text);
        publicBlog = findViewById(R.id.blog_edit_page_public_blog_check);
        saveButton = findViewById(R.id.blog_edit_page_save_button);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: create intent to select an image
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog blog = generateBlog();
                if (blog != null) {
                    // TODO: save blog to firebase
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_blog_menu, menu);
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
            case R.id.menu_my_account:
                intent = new Intent(this, AccountSettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_sign_out:
                // TODO: sign out
                return true;
            case R.id.about_this_app:
                intent = new Intent(this, AboutThisAppActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean editTextHasContent(EditText editText) {
        return editText == null || editText.getText().toString().trim().length() > 0;
    }

    private Blog generateBlog() {
        if (editTextHasContent(titleEditText) && editTextHasContent(bodyEditText) && selectedImageUri != null) {
            return new Blog(
                    titleEditText.getText().toString(),
                    selectedImageUri.toString(),
                    bodyEditText.getText().toString(),
                    // TODO: get creator email from firebase and put it here
                    null,
                    publicBlog.isChecked());
        }   else {
            Toast.makeText(this, R.string.blog_edit_page_blog_not_complete_toast_message, Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
