package com.android.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

    private List<Blog> blogs;
    private Context context;
    private DatabaseReference blogDatabaseReference;

    public BlogAdapter(List<Blog> blogs, Context context) {
        this.blogs = blogs;
        this.context = context;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_layout, viewGroup, false);
        return new BlogViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder blogViewHolder, int i) {
        final Blog blog = blogs.get(i);
        blogViewHolder.getBlogTitleTextView().setText(blog.getTitle());
        Picasso.get().load(blog.getImageUrl()).into(blogViewHolder.getBlogImageView());
        blogViewHolder.getBlogBodyTextView().setText(blog.getBlogContents());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        } else if (user.getEmail().equals(blog.getCreatorEmail())) {
            blogViewHolder.getBlogEditButton().setEnabled(true);
            blogViewHolder.getBlogEditButton().setBackgroundColor(context.getResources().getColor(R.color.activeButtonBackground));
        } else {
            blogViewHolder.getBlogEditButton().setVisibility(View.INVISIBLE);
        }

        blogViewHolder.getBlogEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditBlogActivity.class);
                intent.putExtra(Constants.BLOG_FOR_EDIT_PAGE, blog);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }
}
