package com.codepath.instagram.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


public class SearchUserResultsAdapter extends RecyclerView.Adapter<SearchUserResultsAdapter.SearchUsersViewHolder> {
    private List<InstagramUser> users;

    public SearchUserResultsAdapter(List<InstagramUser> users) {
        this.users = users;
    }

    @Override
    public SearchUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user_result, parent, false);
        return new SearchUsersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchUsersViewHolder holder, int position) {
        final InstagramUser user = users.get(position);
        holder.tvUserName.setText(user.userName);
        holder.tvFullName.setText(user.fullName);
        holder.sdvProfileImage.setImageURI(Uri.parse(user.profilePictureUrl));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void replaceAll(List<InstagramUser> users) {
        this.users.clear();
        this.users.addAll(users);
        this.notifyDataSetChanged();
    }

    public class SearchUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvUserName;
        TextView tvFullName;
        SimpleDraweeView sdvProfileImage;

        public SearchUsersViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullName);
            sdvProfileImage = (SimpleDraweeView)itemView.findViewById(R.id.sdvProfileImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), PhotoGridActivity.class);
            i.putExtra(PhotoGridActivity.EXTRA_USER_ID, users.get(getPosition()).userId);
            view.getContext().startActivity(i);
        }
    }
}
