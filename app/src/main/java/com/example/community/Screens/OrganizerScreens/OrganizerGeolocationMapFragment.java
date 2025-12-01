package com.example.community.Screens.OrganizerScreens;

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

import com.example.community.Event;
import com.example.community.EventService;
import com.example.community.R;
import com.example.community.WaitingListEntry;
import com.example.community.WaitingListEntryService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

/**
 * Fragment for displaying a Google Map with entrant locations for a specific event.
 * Shows markers for each entrant who joined with geolocation data.
 */
public class OrganizerGeolocationMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "GeolocationMapFragment";
    private static final String ARG_EVENT_ID = "event_id";

    private GoogleMap mMap;
    private Button backButton;
    private Event currentEvent;
    private EventService eventService;
    private WaitingListEntryService waitingListEntryService;
    private String eventID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organizer_geolocation_map_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventService = new EventService();
        waitingListEntryService = new WaitingListEntryService();
        backButton = view.findViewById(R. id.mapBackButton);

        // Get event ID from arguments
        if (getArguments() != null) {
            eventID = getArguments().getString(ARG_EVENT_ID);
        }

        if (eventID == null) {
            Toast.makeText(getContext(), "Event ID not found", Toast.LENGTH_SHORT). show();
            NavHostFragment.findNavController(this). navigateUp();
            return;
        }

        backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // Get the SupportMapFragment and request notification when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        loadEventDetails();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadEntrantLocations();
    }

    private void loadEventDetails() {
        eventService.getEvent(eventID)
                .addOnSuccessListener(event -> {
                    currentEvent = event;
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load event details", e);
                    Toast.makeText(getContext(), "Failed to load event details", Toast. LENGTH_SHORT).show();
                });
    }

    private void loadEntrantLocations() {
        if (mMap == null) {
            return;
        }

        waitingListEntryService.getWaitlistEntriesWithLocation(eventID)
                .addOnSuccessListener(entries -> {
                    if (entries == null || entries.isEmpty()) {
                        Toast.makeText(getContext(), "No entrants with location data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LatLng firstLocation = null;
                    int markerCount = 0;

                    for (WaitingListEntry entry : entries) {
                        GeoPoint geoPoint = entry.getJoinLocation();
                        if (geoPoint != null) {
                            LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());

                            // Add marker to map
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .title("Entrant Location")
                                    .snippet("Joined from: " + geoPoint.getLatitude() + ", " + geoPoint.getLongitude());

                            mMap.addMarker(markerOptions);

                            if (firstLocation == null) {
                                firstLocation = location;
                            }
                            markerCount++;
                        }
                    }

                    // Move camera to first location and set zoom level
                    if (firstLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
                    }

                    Toast.makeText(getContext(), "Loaded " + markerCount + " entrant locations", Toast.LENGTH_SHORT). show();
                })
                . addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load entrant locations", e);
                    Toast.makeText(getContext(), "Failed to load entrant locations", Toast.LENGTH_SHORT).show();
                });
    }
}