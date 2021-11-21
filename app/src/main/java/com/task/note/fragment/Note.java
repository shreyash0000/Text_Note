package com.task.note.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.task.note.R;
import com.task.note.adapter.NoteAdapter;
import com.task.note.adapter.model.Notes;
import com.task.note.databaseHandler.DatabaseHandler;
import com.task.note.preference.Preference;
import com.task.note.screen.AddNoteActivity;
import com.task.note.screen.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Note extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    // list && adapter && database
    private List<Notes> notesList = new ArrayList<>();
    private List<Notes> notesList1 = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private DatabaseHandler databaseHandler;

    // filter && Preference
    private SearchView ivFilter;
    private RelativeLayout rlSort;
    private Preference preference;
    private TextView tvSortby;

    public Note() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false); // Inflate fragment View

        // preference
        preference = new Preference(getActivity());

        // findView Class
        findViewClass(view);

        // filter View tab and Sort View
        TextView linearSort = view.findViewById(R.id.title);
        TextView linearView = view.findViewById(R.id.view);

        // filter View

        // simple view linear without description
        setRecyclerView();

        // setViewWhatOpen
        setViewWhatOpen(view);

        // Btn For View list
        linearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout) view.findViewById(R.id.sortRl)).setVisibility(View.VISIBLE);
                ((RelativeLayout) view.findViewById(R.id.view_details)).setVisibility(View.VISIBLE);
                ((RelativeLayout) view.findViewById(R.id.sortDetails)).setVisibility(View.GONE);
                // recently     /*  recyclerView.setVisibility(View.GONE);*/
//                if (!preference.getView()){
                ((TextView) view.findViewById(R.id.smallList)).setText("List");
                ((TextView) view.findViewById(R.id.detailsList)).setText("Details");
                ((TextView) view.findViewById(R.id.gridList)).setText("Grid");
                ((TextView) view.findViewById(R.id.gridLargeList)).setText("Large Grid");
//                }

                /*preference.setView(true);*/
            }
        });

        // btn for Sort list
        linearSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout) view.findViewById(R.id.sortRl)).setVisibility(View.VISIBLE);
                ((RelativeLayout) view.findViewById(R.id.view_details)).setVisibility(View.GONE);
                ((RelativeLayout) view.findViewById(R.id.sortDetails)).setVisibility(View.VISIBLE);
                // recently    /*recyclerView.setVisibility(View.GONE);*/
//                if (preference.getView()){
                ((TextView) view.findViewById(R.id.modify)).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.created)).setText("By Created Date");
                ((TextView) view.findViewById(R.id.alphabetically)).setText("Alphabetically");
                ((TextView) view.findViewById(R.id.reminder)).setText("By Reminder Time");
//                }
                /*preference.setView(false);*/
            }
        });

        //ini class
        initClass();

        // btn for view sort list && view list
        rlSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!preference.getOpen()) {
                    ((RelativeLayout) view.findViewById(R.id.sortRl)).setVisibility(View.VISIBLE);
                    //recently  /*recyclerView.setVisibility(View.GONE);*/

                    // manage View List
                    ManageViewList(view);

                    // preference
                    preference.setOpen(true);
                } else if (preference.getOpen()) {
                    ((RelativeLayout) view.findViewById(R.id.sortRl)).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    // manage View List
                    ManageViewList(view);

                    // preference
                    preference.setOpen(false);
                }
            }
        });


        ((TextView) view.findViewById(R.id.alphabetically)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                Notes[] notesArr1 = new Notes[notesList.size()];
                notesArr1 = notesList.toArray(notesArr1);
                Arrays.sort(notesArr1, Notes.titleComparator);
                notesList = Arrays.asList(notesArr1);
                noteAdapter = new NoteAdapter(getActivity(), notesList);
                recyclerView.setAdapter(noteAdapter);
                tvSortby.setText("Sort By Alphabetically Time");
            }
        });

        ((TextView) view.findViewById(R.id.created)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                notesList1 = databaseHandler.getCreatedNotes();
                noteAdapter = new NoteAdapter(getActivity(), notesList1);
                recyclerView.setAdapter(noteAdapter);
                tvSortby.setText("Sort By Created Date");
            }
        });

        ((TextView) view.findViewById(R.id.smallList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                preference.setViewList(0);

                // manage view
                redirectiing(view);
            }
        });

        ((TextView) view.findViewById(R.id.detailsList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                preference.setViewList(1);

                // manage view
                redirectiing(view);
            }
        });

        ((TextView) view.findViewById(R.id.gridList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                preference.setViewList(2);

                // manage view
                redirectiing(view);
            }
        });

        ((TextView) view.findViewById(R.id.gridLargeList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                preference.setViewList(3);

                // manage view
                redirectiing(view);
            }
        });


        return view;
    }

    // setViewWhatOpen
    private void setViewWhatOpen(View view) {
        if (preference.getView()) {
            ((TextView) view.findViewById(R.id.modify)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.created)).setText("By Created Date");
            ((TextView) view.findViewById(R.id.alphabetically)).setText("Alphabetically");
            ((TextView) view.findViewById(R.id.reminder)).setText("By Reminder Time");
        }

        if (!preference.getView()) {
            ((TextView) view.findViewById(R.id.modify)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.modify)).setText("List");
            ((TextView) view.findViewById(R.id.created)).setText("Details");
            ((TextView) view.findViewById(R.id.alphabetically)).setText("Grid");
            ((TextView) view.findViewById(R.id.reminder)).setText("Large Grid");
        }
    }

    // set Recycler View
    private void setRecyclerView() {
        if (preference.getViewList() == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        // simple view linear with description
        if (preference.getViewList() == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        // grid view with 2 column
        if (preference.getViewList() == 2) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        // grid view with 3 column
        if (preference.getViewList() == 3) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

    }

    // manage View List
    private void ManageViewList(View view) {
        ((TextView) view.findViewById(R.id.modify)).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.created)).setText("By Created Date");
        ((TextView) view.findViewById(R.id.alphabetically)).setText("Alphabetically");
        ((TextView) view.findViewById(R.id.reminder)).setText("By Reminder Time");
        ((TextView) view.findViewById(R.id.smallList)).setText("List");
        ((TextView) view.findViewById(R.id.detailsList)).setText("Details");
        ((TextView) view.findViewById(R.id.gridList)).setText("Grid");
        ((TextView) view.findViewById(R.id.gridLargeList)).setText("Large Grid");
    }

    private void redirectiing(View view) {
        ((RelativeLayout) view.findViewById(R.id.sortRl)).setVisibility(View.GONE);
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    // List && Recycler
    private void initClass() {
        databaseHandler = new DatabaseHandler(getActivity());

        notesList = databaseHandler.getAllNotes();

        noteAdapter = new NoteAdapter(getActivity(), notesList);
        recyclerView.setAdapter(noteAdapter);

        ivFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AddNoteActivity.class);
                startActivity(in);
            }
        });
    }

    // filter
    private void filter(String text) {
        // creating a new array list to filter our data.
        List<Notes> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Notes item : notesList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            Toast.makeText(getActivity(), "Result Not Found", Toast.LENGTH_SHORT).show();
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            // Toast.makeText(getActivity(), "No Note Found..", Toast.LENGTH_SHORT).show();
        } else {
            noteAdapter.filterList(filteredlist);
        }
    }

    // find View Class
    private void findViewClass(View view) {
        ((TextView) view.findViewById(R.id.action_bar_title)).setText("Note"); // custom Action Bar Title
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_add);
        ivFilter = (SearchView) view.findViewById(R.id.ivSearch);
        ivFilter.setVisibility(View.VISIBLE);
        rlSort = (RelativeLayout) view.findViewById(R.id.rlSort);
        tvSortby = (TextView) view.findViewById(R.id.tvSortby);
    }

}