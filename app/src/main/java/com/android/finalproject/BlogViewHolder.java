package com.android.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BlogViewHolder extends RecyclerView.ViewHolder {

    private CardView cardView;
    private TextView blogTitleTextView;
    private Button blogEditButton;
    private ImageView blogImageView;
    private TextView blogBodyTextView;

    public BlogViewHolder(View itemView, final Context context) {
        super(itemView);
        cardView = itemView.findViewById(R.id.blog_card_view);
        blogTitleTextView = itemView.findViewById(R.id.blog_card_view_title_text);
        blogEditButton = itemView.findViewById(R.id.blog_card_view_edit_button);
        blogImageView = itemView.findViewById(R.id.blog_card_view_image);
        blogBodyTextView = itemView.findViewById(R.id.blog_card_view_body_text);
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public TextView getBlogTitleTextView() {
        return blogTitleTextView;
    }

    public void setBlogTitleTextView(TextView blogTitleTextView) {
        this.blogTitleTextView = blogTitleTextView;
    }

    public Button getBlogEditButton() {
        return blogEditButton;
    }

    public void setBlogEditButton(Button blogEditButton) {
        this.blogEditButton = blogEditButton;
    }

    public ImageView getBlogImageView() {
        return blogImageView;
    }

    public void setBlogImageView(ImageView blogImageView) {
        this.blogImageView = blogImageView;
    }

    public TextView getBlogBodyTextView() {
        return blogBodyTextView;
    }

    public void setBlogBodyTextView(TextView blogBodyTextView) {
        this.blogBodyTextView = blogBodyTextView;
    }
}
