package com.example.community.Screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.community.R;

public class FilterFragment extends Fragment {

    private EditText inputKeyword;
    private EditText inputTime;
    private Button buttonBack;
    private Button buttonApplyFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputKeyword = view.findViewById(R.id.inputKeyword);
        inputTime = view.findViewById(R.id.inputTimeAvailable);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonApplyFilter = view.findViewById(R.id.buttonApplyFilter);

        buttonBack.setOnClickListener(v ->
                NavHostFragment.findNavController(FilterFragment.this).popBackStack()
        );

        buttonApplyFilter.setOnClickListener(v -> {
            String keyword = inputKeyword.getText().toString().trim();
            String time = inputTime.getText().toString().trim();

            // ⬇️ IMPORTANT: we allow both to be empty!
            // Empty keyword + empty time means "clear filter / show all events"

            Bundle filters = new Bundle();
            filters.putString("keyword", keyword);
            filters.putString("time", time);

            var navController = NavHostFragment.findNavController(FilterFragment.this);
            if (navController.getPreviousBackStackEntry() != null) {
                navController.getPreviousBackStackEntry()
                        .getSavedStateHandle()
                        .set("eventFilters", filters);
            }

            navController.popBackStack();
        });
    }
}
