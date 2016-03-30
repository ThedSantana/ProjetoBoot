package com.example.android.blocodenotas.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.blocodenotas.R;
import com.example.android.blocodenotas.activities.NoteEditorActivity;
import com.example.android.blocodenotas.adapters.NoteListAdapter;
import com.example.android.blocodenotas.data.NoteManager;
import com.example.android.blocodenotas.models.Note;
import com.melnykov.fab.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {

    private FloatingActionButton mFab;
    private View mRootView;
    private List<Note> mNotes;
    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private void makeToast(String mensagem){
        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
    }

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and hold the reference
        //in mRootView
        mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);

        //Get a programmatic reference to the Floating Action Button
        mFab = (FloatingActionButton)mRootView.findViewById(R.id.fab);

        //attach an onClick listener to the Floating Action Button
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NoteEditorActivity.class));
            }
        });

        setupList();
        return mRootView;
    }



    private void setupList() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final GestureDetector mGestureDetector =
                new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildLayoutPosition(child);
                    Note selectedNote = mNotes.get(position);
                    Intent editorIntent = new Intent(getActivity(), NoteEditorActivity.class);
                    editorIntent.putExtra("id", selectedNote.getId());
                    startActivity(editorIntent);


                    //now we have the selected note

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mNotes = NoteManager.newInstance(getActivity()).getAllNotes();
        mAdapter = new NoteListAdapter(mNotes, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }


    public void sortByTitle (){
        mNotes = NoteManager.newInstance(getActivity()).getAllNotes();
        Collections.sort(mNotes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        mAdapter = new NoteListAdapter(mNotes,getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }
    public void sortByCreationTime (List<Note> allNotes) {
        Collections.sort(mNotes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                return lhs.getDateCreated().compareTo(rhs.getDateCreated());
            }
        });
    }
    public void sortByModificationTime (List<Note> allNotes) {
        Collections.sort(mNotes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                return lhs.getDataModified().compareTo(rhs.getDataModified());
            }
        });
    }


}