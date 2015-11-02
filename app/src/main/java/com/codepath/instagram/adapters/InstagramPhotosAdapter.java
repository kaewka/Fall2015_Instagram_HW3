package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramPhotosAdapter
        extends RecyclerView.Adapter<InstagramPhotosAdapter.PhotoViewHolder> {
    private List<InstagramPost> posts;

    public InstagramPhotosAdapter(List<InstagramPost> posts) {
        this.posts = (posts == null ? new ArrayList<InstagramPost>() : posts);
    }

    @Override
    public InstagramPhotosAdapter.PhotoViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_photo, parent, false);

        PhotoViewHolder viewHolder = new PhotoViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramPhotosAdapter.PhotoViewHolder holder, int position) {
        final InstagramPost instagramPost = posts.get(position);
        holder.sdvPhoto.setImageURI(Uri.parse(instagramPost.image.imageUrl));
        holder.sdvPhoto.setAspectRatio((float) instagramPost.image.imageWidth / instagramPost.image.imageHeight);
    }

    @Override
    public int getItemCount() {
        return (posts == null ? 0 : posts.size());
    }

    public static final class PhotoViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            sdvPhoto = (SimpleDraweeView) itemView.findViewById(R.id.sdvPhoto);
        }
    }

    public void replaceAll(List<InstagramPost> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        this.notifyDataSetChanged();
    }
}
