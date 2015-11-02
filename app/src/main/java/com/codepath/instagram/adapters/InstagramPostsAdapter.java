package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramPostsViewHolder> {
    private static ArrayList<InstagramPost> posts;
    private static Context context;

    public InstagramPostsAdapter(ArrayList<InstagramPost> posts) {
        this.posts = posts;
    }

    @Override
    public InstagramPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_post, parent, false);

        InstagramPostsViewHolder viewHolder = new InstagramPostsViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InstagramPostsViewHolder holder, int position) {
        final InstagramPost post = posts.get(position);

        holder.tvUserName.setText(post.user.userName);

        ForegroundColorSpan blueForegroundColorSpan = new ForegroundColorSpan(
                context.getResources().getColor(R.color.blue_text));
        TypefaceSpan serifMediumTypeFaceSpan = new TypefaceSpan("sans-serif-medium");
        SpannableStringBuilder ssb = new SpannableStringBuilder(post.user.userName);
        ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(serifMediumTypeFaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(" ");
        if (post.caption != null) {
            ssb.append(post.caption);
            ForegroundColorSpan grayForeGroundColorSpan = new ForegroundColorSpan(
                    context.getResources().getColor(R.color.gray_text));
            TypefaceSpan serifTypeFaceSpan = new TypefaceSpan("sans-serif");
            ssb.setSpan(grayForeGroundColorSpan, ssb.length() - post.caption.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(serifTypeFaceSpan, ssb.length() - post.caption.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.tvCaption.setText(ssb, TextView.BufferType.NORMAL);
        holder.tvCreatedTime.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000));
        holder.tvLikesCount.setText(Utils.formatNumberForDisplay(post.likesCount) + " likes");
        holder.ivAvatar.setImageURI(Uri.parse(post.user.profilePictureUrl));
        holder.ivImage.setImageURI(Uri.parse(post.image.imageUrl));
        holder.ivImage.setAspectRatio((float) post.image.imageWidth / post.image.imageHeight);
        holder.tvCommentsCount.setText("View all " + post.commentsCount + " comments");
        if (post.commentsCount == 0) {
            holder.tvCommentsCount.setVisibility(View.GONE);
        } else if(post.commentsCount <= 2) {
            holder.tvCommentsCount.setVisibility(View.INVISIBLE);
        }

        List<InstagramComment> comments = post.comments;
        holder.llComments.removeAllViews();
        if (post.commentsCount == 1) {
            ssb = new SpannableStringBuilder(post.comments.get(0).user.userName);
            ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(serifMediumTypeFaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(" ");
            ssb.append(post.comments.get(0).text);
            View view = LayoutInflater.from(context).inflate(R.layout.item_text_comment, holder.llComments, false);
            TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvComment.setText(ssb, TextView.BufferType.NORMAL);
            holder.llComments.addView(view);
        } else if (post.commentsCount >= 2) {
            for (int i = post.comments.size() - 1; i >= post.comments.size() - 2; i--) {
                ssb = new SpannableStringBuilder(post.comments.get(0).user.userName);
                ssb.setSpan(blueForegroundColorSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(serifMediumTypeFaceSpan, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append(" ");
                ssb.append(post.comments.get(i).text);
                View view = LayoutInflater.from(context).inflate(R.layout.item_text_comment, holder.llComments, false);
                TextView tvComment = (TextView) view.findViewById(R.id.tvComment);
                tvComment.setText(ssb, TextView.BufferType.NORMAL);
                holder.llComments.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class InstagramPostsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName;
        public TextView tvCaption;
        public SimpleDraweeView ivAvatar;
        public SimpleDraweeView ivImage;
        public TextView tvLikesCount;
        public TextView tvCreatedTime;
        public TextView tvCommentsCount;
        public LinearLayout llComments;
        public ImageButton ibShare;

        public InstagramPostsViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvLikesCount = (TextView) itemView.findViewById(R.id.tvLikesCount);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
            ivAvatar = (SimpleDraweeView) itemView.findViewById(R.id.ivAvatar);
            ivImage = (SimpleDraweeView) itemView.findViewById(R.id.ivImage);
            llComments = (LinearLayout) itemView.findViewById(R.id.llComments);
            tvCommentsCount = (TextView) itemView.findViewById(R.id.tvCommentsCount);
            ibShare = (ImageButton) itemView.findViewById(R.id.ibShare);
            setUpListeners();
        }

        private void setUpListeners() {
            tvCommentsCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context, CommentsActivity.class);
                    i.putExtra("mediaId", posts.get(getPosition()).mediaId);
                    context.startActivity(i);
                }
            });
            ibShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Context context = view.getContext();
                    PopupMenu popup = new PopupMenu(context, view);
                    // Inflate the menu from xml
                    popup.getMenuInflater().inflate(R.menu.popup_filter, popup.getMenu());
                    // Setup menu item selection
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_share:
                                    ivImage.buildDrawingCache();
                                    Bitmap bitmap = ivImage.getDrawingCache();

                                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                            bitmap, "Image Description", null);
                                    Uri bmpUri = Uri.parse(path);
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                    shareIntent.setType("image/*");
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    context.startActivity(Intent.createChooser(shareIntent, "Share images..."));
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    // Show the menu
                    popup.show();
                }
            });
        }
    }
}
