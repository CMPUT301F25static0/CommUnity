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
import com.example.community.UserRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageArrayAdapter extends RecyclerView.Adapter<ImageArrayAdapter.ImageViewHolder> {

    private final List<Image> imageList;
    private final OnImageDeleteListener listener;

    public interface OnImageDeleteListener {
        void onDeleteClick(Image image, int position);
    }
    public ImageArrayAdapter(List<Image> imageList, OnImageDeleteListener listener) {
        this.imageList = imageList;
        this.listener = listener;
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
        String uploaderID = image.getUploadedBy();

        if (uploaderID != null && !uploaderID.isEmpty()) {
            UserRepository userRepository = new UserRepository();

            userRepository.getByUserID(uploaderID)
                    .addOnSuccessListener(user -> {
                        if (user != null) {
                            String displayName = (user.getUsername() != null && !user.getUsername().isEmpty())
                                    ? user.getUsername()
                                    : "Unknown Name";

                            holder.imageInfo.setText("Uploaded by: \n" + displayName);
                        } else {
                            holder.imageInfo.setText("Uploaded by: \nUnknown User");
                        }
                    })
                    .addOnFailureListener(e -> {
                        holder.imageInfo.setText("Uploaded by: Error loading name");
                    });
        } else {
            holder.imageInfo.setText("Uploaded by: \nUnknown");
        }

        if (image.getImageURL() != null && !image.getImageURL().isEmpty()) {
            Picasso.get()
                    .load(image.getImageURL())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(image, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public TextView imageInfo;
        Button deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgContent);
            imageInfo = itemView.findViewById(R.id.posterName);
            deleteButton = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
