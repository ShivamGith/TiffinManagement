package com.svd.tiffinmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import Models.TiffinCenter;

public class TiffinCenterAdapter extends RecyclerView.Adapter<TiffinCenterAdapter.ViewHolder> {

    private Context context;
    private List<TiffinCenter> tiffinCenters;

    public TiffinCenterAdapter(Context context, List<TiffinCenter> tiffinCenters) {
        this.context = context;
        this.tiffinCenters = tiffinCenters;
    }
    public void SetTiffinCentersList(List<TiffinCenter> tiffinCenters)
    {
        this.tiffinCenters = tiffinCenters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tiffin_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TiffinCenter tiffinCenter = tiffinCenters.get(position);
        holder.bind(tiffinCenter);
    }

    @Override
    public int getItemCount() {
        return tiffinCenters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewName, textViewCuisines, textViewLocation;
        RatingBar ratingBar;
        Button btnMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCuisines = itemView.findViewById(R.id.textViewCuisines);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnMenu = itemView.findViewById(R.id.btnMenu);
        }

        public void bind(TiffinCenter tiffinCenter) {
            // Bind data to views
            Glide.with(context).load(tiffinCenter.getThumbnail()).into(imageViewThumbnail);
            textViewName.setText(tiffinCenter.getName());
            textViewCuisines.setText(tiffinCenter.getCuisines());
            textViewLocation.setText(tiffinCenter.getLocation());
            String ratingString = tiffinCenter.getRating();
            if (ratingString != null) {
                float ratingFloat = Float.parseFloat(ratingString.trim());
                ratingBar.setRating(ratingFloat);
            } else {
                ratingBar.setRating(5);
            }

            // You can bind click listeners or other functionalities here if needed
        }
    }
}