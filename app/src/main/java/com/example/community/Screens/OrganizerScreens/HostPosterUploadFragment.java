package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class HostPosterUploadFragment extends Fragment {

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

        Button buttonUpload = view.findViewById(R.id.buttonUploadPoster);
        Button buttonSubmitPoster = view.findViewById(R.id.buttonSubmitPoster);

        // Button buttonCancel = view.findViewById(R.id.buttonCancel);

        buttonUpload.setOnClickListener(v ->
                Toast.makeText(getActivity(), "Upload Poster", Toast.LENGTH_SHORT).show());

        // Submit → go back to Host main page
        buttonSubmitPoster.setOnClickListener(v ->
                NavHostFragment.findNavController(HostPosterUploadFragment.this)
                        .navigate(R.id.action_HostPosterUploadFragment_to_OrganizerHomeFragment)
        );
    }
}
