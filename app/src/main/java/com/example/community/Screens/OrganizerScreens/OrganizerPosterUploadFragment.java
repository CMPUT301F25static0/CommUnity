package com.example.community.Screens.OrganizerScreens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.ImageService;
import com.example.community.R;
import com.example.community.UserService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class OrganizerPosterUploadFragment extends Fragment {
    private static final String TAG = "OrganizerPosterUploadFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private String eventId;
    private byte[] selectedImageData;
    private String currentOrganizerId;

    private TextView previewTextLabel;
    private ImageView imagePosterPreviewImageView;
    private Button buttonUploadPoster, buttonSubmitPoster, cancelButton;
    private ProgressBar progressBar;

    private ImageService imageService;
    private UserService userService;

    private ActivityResultLauncher<Intent> imagePicker;
    private ActivityResultLauncher<String> requestPermissionsLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.organizer_poster_upload_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageService = new ImageService();
        userService = new UserService();

        eventId = getArguments().getString(ARG_EVENT_ID);
        if (eventId == null) {
            Toast.makeText(getActivity(), "Event ID is not valid", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
            return;
        }

        previewTextLabel = view.findViewById(R.id.previewTextLabel);
        imagePosterPreviewImageView = view.findViewById(R.id.imagePosterPreview);
        buttonUploadPoster = view.findViewById(R.id.buttonUploadPoster);
        buttonSubmitPoster = view.findViewById(R.id.buttonSubmitPoster);
        cancelButton = view.findViewById(R.id.cancelButton);
        progressBar = view.findViewById(R.id.uploadProgressBar);

        String deviceToken = userService.getDeviceToken();
        userService.getUserIDByDeviceToken(deviceToken)
                .addOnSuccessListener(userId -> currentOrganizerId = userId)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get the user ID for the current organizer");
                    Toast.makeText(getActivity(), "Failed to get the organizer information", Toast.LENGTH_SHORT).show();
                });

        buttonUploadPoster.setOnClickListener(v -> openImagePicker());
        buttonSubmitPoster.setOnClickListener(v -> uploadImage());
        cancelButton.setOnClickListener(v -> clearImage());

        imagePicker();
        requestPermissions();
    }

    private void imagePicker() {
        imagePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Uri imageUri = data.getData();


                        if (imageUri != null) {
                            previewTextLabel.setVisibility(View.VISIBLE);
                            Picasso.get()
                                    .load(imageUri)
                                    .fit()
                                    .centerCrop()
                                    .into(imagePosterPreviewImageView);
                            imagePosterPreviewImageView.setVisibility(View.VISIBLE);

                            try {
                                selectedImageData = getBytesFromUri(imageUri);
                                buttonSubmitPoster.setEnabled(true);
                                Toast.makeText(getContext(), "Image selected. Ready to be uploaded", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Log.e(TAG, "Failed to read image data", e);
                                Toast.makeText(getActivity(), "Failed to read image data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void requestPermissions() {
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        Toast.makeText(getActivity(), "Permission denied. Please grant permission to upload an image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImagePicker() {
        if (areMediaPermissionsGranted()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePicker.launch(intent);
        } else {
            requestMediaPermissions();
        }
    }

    private boolean areMediaPermissionsGranted() {
        boolean isImagePermissionGranted = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isImagePermissionGranted = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            isImagePermissionGranted = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return isImagePermissionGranted;
    }

    private void requestMediaPermissions() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        requestPermissionsLauncher.launch(permission);
    }

    private void clearImage() {
        selectedImageData = null;
        buttonSubmitPoster.setEnabled(false);
        previewTextLabel.setVisibility(View.GONE);
        imagePosterPreviewImageView.setVisibility(View.GONE);
    }

    /**
     * Converts an image Uri to a byte array for Firebase upload.
     * Reads the image file in chunks and stores all bytes in memory.
     *
     * @param uri The Uri of the selected image
     * @return A byte array containing all the image data
     * @throws IOException If the file cannot be read
     */
    private byte[] getBytesFromUri(Uri uri) throws IOException {
        // Open an input stream from the Uri to read the image file
        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);

        // Create a ByteArrayOutputStream to store all bytes in memory
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // Set the chunk size to 1024 bytes (1 KB)
        int bufferSize = 1024;

        // Create a temporary byte array to hold each chunk as we read it
        byte[] buffer = new byte[bufferSize];

        // Variable to store how many bytes were read in each iteration
        int len;

        // Loop: Keep reading chunks until we reach the end of the file
        while ((len = inputStream.read(buffer)) != -1) {
            // Write the bytes we just read (len bytes) to our byteBuffer
            byteBuffer.write(buffer, 0, len);
        }

        // Close the input stream to release system resources
        inputStream.close();

        // Convert the ByteArrayOutputStream to a byte array and return it
        return byteBuffer.toByteArray();
    }

    private void uploadImage() {
        if (selectedImageData == null) {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buttonSubmitPoster.setEnabled(false);
        cancelButton.setEnabled(false);

        imageService.uploadEventPoster(eventId, selectedImageData, currentOrganizerId, true)
                .addOnSuccessListener(imageUrl -> {
                    progressBar.setVisibility(View.GONE);
                    buttonSubmitPoster.setEnabled(true);
                    cancelButton.setEnabled(true);
                    Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    buttonSubmitPoster.setEnabled(true);
                    cancelButton.setEnabled(true);
                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }
}
