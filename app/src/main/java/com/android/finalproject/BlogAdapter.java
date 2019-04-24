package com.android.finalproject;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

    private List<Blog> blogs;
    private Context context;

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
        blogViewHolder.getBlogImageView().setImageURI(Uri.parse(blog.getImageUrl()));
        blogViewHolder.getBlogBodyTextView().setText(blog.getBlogContents());
        blogViewHolder.setCreatorEmail(blog.getCreatorEmail());
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }
}
