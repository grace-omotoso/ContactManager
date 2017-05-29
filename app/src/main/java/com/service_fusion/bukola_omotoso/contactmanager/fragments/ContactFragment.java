package com.service_fusion.bukola_omotoso.contactmanager.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.service_fusion.bukola_omotoso.contactmanager.R;
import com.service_fusion.bukola_omotoso.contactmanager.ContactsAdapter;
import com.service_fusion.bukola_omotoso.contactmanager.data.DatabaseDescription;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CONTACTS_LOADER = 0;
    private ContactFragmentListener contactFragmentListener;
    private ContactsAdapter contactsAdapter;

    public ContactFragment() {
    }

    public interface ContactFragmentListener   {
        void onContactSelected(Uri contactUri);
        void onContactAdded();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        contactsAdapter = new ContactsAdapter(new ContactsAdapter.ContactClickListener() {
            @Override
            public void onClick(Uri contactUri) {
                contactFragmentListener.onContactSelected(contactUri);
            }
        });

        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton actionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactFragmentListener.onContactAdded();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        contactFragmentListener = (ContactFragmentListener) context;
    }

    @Override
    public void onDetach()  {
        super.onDetach();
        contactFragmentListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)    {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
    }

    public void updateContactList() {
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case CONTACTS_LOADER:
                return new CursorLoader(getActivity(), DatabaseDescription.Contact.CONTENT_URI, null, null, null, DatabaseDescription.Contact.COLUMN_FIRSTNAME + " COLLATE NOCASE ASC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactsAdapter.swapCursor(null);
    }
}
