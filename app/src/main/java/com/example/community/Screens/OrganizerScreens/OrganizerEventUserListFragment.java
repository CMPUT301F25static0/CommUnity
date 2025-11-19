package com.example.community.Screens.OrganizerScreens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.ArrayAdapters.UserArrayAdapter;
import com.example.community.R;
import com.example.community.User;
import com.example.community.UserService;
import com.example.community.WaitingListEntryService;

import java.util.List;

public class OrganizerEventUserListFragment extends DialogFragment {

    public static final String TAG = "OrganizerEventUserListFragment";
    private static final String ARG_EVENT_ID = "event_id";
    private static final String ARG_LIST_TYPE = "list_type";

    private String eventId;
    private String listType;
    public List<User> usersList;

    private RecyclerView userListRecyclerView;
    private UserArrayAdapter userArrayAdapter;
    private TextView listTitle;
    private Button closeListButton;

    private WaitingListEntryService waitingListEntryService;
    private UserService userService;

    public static OrganizerEventUserListFragment newInstance(String eventId, String listType) {
        OrganizerEventUserListFragment fragment = new OrganizerEventUserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        args.putString(ARG_LIST_TYPE, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_user_lists_dialog, container, false);
        return view;
    }


}
