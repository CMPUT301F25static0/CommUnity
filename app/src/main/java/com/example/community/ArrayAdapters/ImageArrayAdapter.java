package com.example.community.ArrayAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.Image;
import com.example.community.R;


import java.util.List;

public class ImageArrayAdapter extends RecyclerView.Adapter<ImageArrayAdapter.ImageViewHolder> {

    private final List<Image> imageList;
    private final OnImageClickListener listener;

    public interface OnImageClickListener {
        void onItemClick(Image image);
        void onDeleteClick(Image image, int position);
    }

    public ImageArrayAdapter(List<Image> imageList) {
        this.imageList = imageList;
        this.listener = null;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = imageList.get(position);

        String displayText = "Uploaded by: " + (image.getUploadedBy() != null ? image.getUploadedBy() : "Unknown");
        holder.imageInfo.setText(displayText);

        holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery); // Fallback icon

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(image);
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(image, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageInfo;
        Button deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgContent);
            imageInfo = itemView.findViewById(R.id.posterName);
            deleteButton = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
