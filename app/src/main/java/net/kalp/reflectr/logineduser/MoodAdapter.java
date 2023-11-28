package net.kalp.reflectr.logineduser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import net.kalp.reflectr.DefaultMoods;
import net.kalp.reflectr.R;
import net.kalp.reflectr.models.Mood;

import java.util.ArrayList;
import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {

        private final List<Mood> moods;
        private final Context context;
        private final OnItemClickListener listener;

        public MoodAdapter(List<Mood> moods, Context context,OnItemClickListener listener) {
            this.moods = moods;
            this.context = context;
            this.listener=listener;
        }

    public interface OnItemClickListener {
        void onItemClick(Mood item);
    }

        @NonNull
        @Override
        public MoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_list_grid, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MoodAdapter.ViewHolder holder, int position) {
            Mood mood = moods.get(position);
            holder.moodName.setText(mood.getTitle());
            Glide.with(context).setDefaultRequestOptions(DefaultMoods.sad.getOptions()).load(mood.getEmoji()).override(100).into(holder.moodImage);
            holder.bind(mood,listener);
        }

        @Override
        public int getItemCount() {
            return moods.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView moodName;
            ImageView moodImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                moodName = itemView.findViewById(R.id.moodTitle);
                moodImage = itemView.findViewById(R.id.emoticon);
            }

            public void bind(Mood mood, OnItemClickListener listener) {
                itemView.setOnClickListener(v -> listener.onItemClick(mood));
            }
        }
}
