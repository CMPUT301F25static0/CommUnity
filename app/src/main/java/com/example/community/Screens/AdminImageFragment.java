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
import com.example.community.Event;

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
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    imagesArrayList.clear();

                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Event event = document.toObject(Event.class);

                            if (event.getPosterImageURL() != null && !event.getPosterImageURL().isEmpty()) {

                                Image img = new Image();
                                img.setImageID(event.getPosterImageID());
                                img.setImageURL(event.getPosterImageURL());

                                if (event.getOrganizerID() != null) {
                                    img.setUploadedBy(event.getOrganizerID());
                                } else {
                                    img.setUploadedBy("Unknown Organizer");
                                }

                                imagesArrayList.add(img);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing event for poster", e);
                        }
                    }

                    imageArrayAdapter.notifyDataSetChanged();

                    if (imagesArrayList.isEmpty()) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "No event posters found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading event posters", e);
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
                    com.google.firebase.firestore.FirebaseFirestore.getInstance()
                            .collection("events")
                            .whereEqualTo("posterImageID", image.getImageID())
                            .limit(1)
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                Event event = querySnapshot.getDocuments().get(0).toObject(Event.class);
                                    imageService.deleteEventPoster(event.getEventID())
                                            .addOnSuccessListener(aVoid -> {
                                                if (position >= 0 && position < imagesArrayList.size()) {
                                                    imagesArrayList.remove(position);
                                                    imageArrayAdapter.notifyItemRemoved(position);
                                                    imageArrayAdapter.notifyItemRangeChanged(position, imagesArrayList.size() - position);
                                                }
                                                Toast.makeText(getContext(), "Image deleted", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e(TAG, "Delete failed", e);
                                                Toast.makeText(getContext(), "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
