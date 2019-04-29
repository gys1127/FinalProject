package com.android.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.android.finalproject.Constants.BLOG_IMAGE_TYPE;
import static com.android.finalproject.Constants.FIREBASE_BLOG_COLLECTION_NAME;
import static com.android.finalproject.Constants.FIREBASE_STORAGE_NAME;

public class EditBlogActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2;

    private EditText titleEditText;
    private ImageButton selectImageButton;
    private EditText bodyEditText;
    private CheckBox publicBlog;
    private Button saveButton;

    private Uri selectedImageUri;
    private Blog blog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference blogDatabaseReference;

    private FirebaseStorage firebaseStorage;
    private StorageReference imageStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);

        firebaseDatabase = FirebaseDatabase.getInstance();
        blogDatabaseReference = firebaseDatabase.getReference().child(FIREBASE_BLOG_COLLECTION_NAME);

        firebaseStorage = FirebaseStorage.getInstance();
        imageStorageReference = firebaseStorage.getReference().child(FIREBASE_STORAGE_NAME);

        titleEditText = findViewById(R.id.blog_edit_page_title_edit_text);
        selectImageButton = findViewById(R.id.blog_edit_page_image_button);
        bodyEditText = findViewById(R.id.blog_edit_page_body_edit_text);
        publicBlog = findViewById(R.id.blog_edit_page_public_blog_check);
        saveButton = findViewById(R.id.blog_edit_page_save_button);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(BLOG_IMAGE_TYPE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "image"), RC_PHOTO_PICKER);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blog blog = generateBlog();
                if (blog != null) {
                    blogDatabaseReference.child(blog.getId()).setValue(blog);
                    finish();
                } else {
                    Toast.makeText(EditBlogActivity.this, R.string.blog_edit_page_blog_not_complete_toast_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.BLOG_FOR_EDIT_PAGE)) {
            blog = (Blog) intent.getSerializableExtra(Constants.BLOG_FOR_EDIT_PAGE);
            titleEditText.setText(blog.getTitle());
            selectedImageUri = Uri.parse(blog.getImageUrl());
            Picasso.get().load(selectedImageUri).into(selectImageButton);
            bodyEditText.setText(blog.getBlogContents());
            publicBlog.setSelected(blog.isPublicBlog());
        }
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
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.about_this_app:
                intent = new Intent(this, AboutThisAppActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                final StorageReference imageRef = imageStorageReference.child(imageUri.getLastPathSegment());
                imageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            selectedImageUri = task.getResult();
                            Picasso.get().load(selectedImageUri).into(selectImageButton);
                        } else {
                            Toast.makeText(EditBlogActivity.this, R.string.blog_edit_page_fail_to_load_image, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private boolean editTextHasContent(EditText editText) {
        return editText == null || editText.getText().toString().trim().length() > 0;
    }

    private Blog generateBlog() {
        if (editTextHasContent(titleEditText) && editTextHasContent(bodyEditText) && selectedImageUri != null) {
            if (blog == null) {
                blog = new Blog();
            }
            blog.setTitle(titleEditText.getText().toString());
            blog.setImageUrl(selectedImageUri.toString());
            blog.setBlogContents(bodyEditText.getText().toString());
            blog.setCreatorEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            blog.setPublicBlog(publicBlog.isChecked());
            return blog;
        } else {
            return null;
        }
    }
}
