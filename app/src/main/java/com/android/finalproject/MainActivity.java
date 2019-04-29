package com.android.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.finalproject.Constants.FIREBASE_BLOG_COLLECTION_NAME;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 100;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference blogDatabaseReference;
    private ChildEventListener blogEventListener;

    private List<Blog> blogs;
    private BlogAdapter blogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        blogs = new ArrayList<>();
        blogAdapter = new BlogAdapter(blogs, this);
        RecyclerView recyclerView = findViewById(R.id.blog_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(blogAdapter);

        blogDatabaseReference = FirebaseDatabase.getInstance().getReference().child(FIREBASE_BLOG_COLLECTION_NAME);
        attachBlogEventListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_add_blog:
                intent = new Intent(this, EditBlogActivity.class);
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
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        attachBlogEventListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachBlogEventListener();
        cleanUp();
    }

    private void attachBlogEventListener() {
        if (blogEventListener == null) {
            blogEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    if (blog != null && firebaseAuth.getCurrentUser() != null && (blog.isPublicBlog() || blog.getCreatorEmail().equals(firebaseAuth.getCurrentUser().getEmail()))) {
                        blogs.add(blog);
                        blogAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    if (blog != null && firebaseAuth.getCurrentUser() != null && (blog.isPublicBlog() || blog.getCreatorEmail().equals(firebaseAuth.getCurrentUser().getEmail()))) {
                        for (int i = 0; i < blogs.size(); i++) {
                            if (blogs.get(i).getId().equals(blog.getId())) {
                                blogs.set(i, blog);
                                break;
                            }
                        }
                        blogAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            blogDatabaseReference.addChildEventListener(blogEventListener);
        }
    }

    private void detachBlogEventListener() {
        if (blogEventListener != null) {
            blogDatabaseReference.removeEventListener(blogEventListener);
            blogEventListener = null;
        }
    }

    private void cleanUp() {
        blogs.clear();
        blogAdapter.notifyDataSetChanged();
    }
}
