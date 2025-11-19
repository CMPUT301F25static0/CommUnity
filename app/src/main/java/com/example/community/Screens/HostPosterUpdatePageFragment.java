package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class HostPosterUpdatePageFragment extends Fragment {

    private ImageView posterImageView;
    private Button buttonUploadPoster;
    private Button buttonRemovePoster;
    private Button buttonSubmitPoster;
    private ImageButton backButton;
    private TextView headerTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.host_upload_poster_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        headerTitle = view.findViewById(R.id.headerTitle);
        posterImageView = view.findViewById(R.id.imagePosterPreview);
        buttonUploadPoster = view.findViewById(R.id.buttonUploadPoster);
        //buttonRemovePoster = view.findViewById(R.id.buttonRemovePoster);
        buttonSubmitPoster = view.findViewById(R.id.buttonSubmitPoster);
        //backButton = view.findViewById(R.id.buttonBack);


        headerTitle.setText("Update Event Poster");



        // Back Button Logic
        if (backButton != null) {
            backButton.setOnClickListener(v ->
                    NavHostFragment.findNavController(HostPosterUpdatePageFragment.this).navigateUp()
            );
        }

        // Upload Poster Logic (Placeholder for Image Picker)
        buttonUploadPoster.setOnClickListener(v -> {
            // TODO: Implement Image Picker logic here
            Toast.makeText(getActivity(), "Select Image logic goes here", Toast.LENGTH_SHORT).show();
        });

        // Remove Poster Logic
        if (buttonRemovePoster != null) {
            buttonRemovePoster.setOnClickListener(v -> {
                // TODO: Reset ImageView to default placeholder and clear selected image uri
                posterImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                Toast.makeText(getActivity(), "Poster removed", Toast.LENGTH_SHORT).show();
            });
        }

        // Submit Logic
        buttonSubmitPoster.setOnClickListener(v -> {
            // TODO: Add Firebase Storage upload logic here

            Toast.makeText(getActivity(), "Poster Updated Successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the Home page
            NavHostFragment.findNavController(HostPosterUpdatePageFragment.this)
                    .navigate(R.id.action_HostPosterUploadFragment_to_OrganizerHomeFragment);
        });
    }
}
