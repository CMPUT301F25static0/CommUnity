package com.example.community.Screens;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.ImageArrayAdapter;
import com.example.community.ImageService;
import com.example.community.R;
import com.example.community.Image;
import java.util.ArrayList;

public class AdminImageFragment extends Fragment {

    Button backButton;

    private ArrayList<com.example.community.Image> imagesArrayList;
    private ImageArrayAdapter imageArrayAdapter;
    private ImageService imageService;

    private RecyclerView adminImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_image_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminImageView = view.findViewById(R.id.imageView);
        backButton = view.findViewById(R.id.buttonBack);

        imageService = new ImageService();
        imagesArrayList = new ArrayList<>();

        adminImageView.setLayoutManager(new LinearLayoutManager(getContext()));

        imageArrayAdapter = new ImageArrayAdapter(imagesArrayList, new ImageArrayAdapter.OnImageDeleteListener() {
            @Override
            public void onDeleteClick(Image image, int position) {
                onDeleteClicked(image, position);
            }
        });

        adminImageView.setAdapter(imageArrayAdapter);

        loadImages();
        setUpClickListener();
    }

    private void loadImages() {
        imageService.listAllImages()
                .addOnSuccessListener(images -> {
                    imagesArrayList.clear();
                    imagesArrayList.addAll(images);
                    imageArrayAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading images", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Failed to load images", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpClickListener() {
        backButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AdminImageFragment.this).navigateUp();
        });
    }

    public void onDeleteClicked(Image image, int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Assuming imageService has a delete method. If not, ensure this matches your service.
                    new ImageService().deleteEventPoster(image.getImageID())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) { // Check if task was successful
                                    if (position >= 0 && position < imagesArrayList.size()) {
                                        imagesArrayList.remove(position);
                                        imageArrayAdapter.notifyItemRemoved(position);
                                        imageArrayAdapter.notifyItemRangeChanged(position, imagesArrayList.size() - position);
                                    }
                                    Toast.makeText(getContext(), "Image deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete image", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to delete image", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Cancel", null).show();
    }
}
