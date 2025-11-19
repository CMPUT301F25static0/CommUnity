package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class FilterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Use your user_filter_page.xml
        return inflater.inflate(R.layout.filter_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonBack = view.findViewById(R.id.buttonBack);
        Button buttonApply = view.findViewById(R.id.buttonApplyFilter);

        // Back → just go back to the previous screen (EntrantHomeFragment)
        buttonBack.setOnClickListener(v ->
                NavHostFragment.findNavController(FilterFragment.this)
                        .popBackStack()
        );

        // Apply → later you can read filters; for now just go back
        buttonApply.setOnClickListener(v -> {
            // TODO: read selected interests + time and apply filter
            NavHostFragment.findNavController(FilterFragment.this)
                    .popBackStack();
        });
    }
}

