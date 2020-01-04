package com.partenie.alex.filatelie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.partenie.alex.filatelie.database.model.CollectionItem;
import com.partenie.alex.filatelie.database.service.CollectionItemService;
import com.partenie.alex.filatelie.util.CollectionItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    public static ArrayList<CollectionItem> collectionItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        getItemsFromDatabase(view);
//        initComp(view);
        return view;
    }

    void initComp(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_gallery);
        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(getString(R.string.SETTINGS_KEY), Context.MODE_PRIVATE);
        final ArrayList<CollectionItem> createLists = prepareData();
        final CollectionItemAdapter adapter = new CollectionItemAdapter(this, createLists);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), sharedPreferences.getInt(getString(R.string.ITEM_PER_COLLUMN_KEY), 1) + 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CollectionItemAdapter.ADD_ITEM_CODE
                && resultCode == RESULT_OK
                && data != null) {
            CollectionItem collectionItem = data.getParcelableExtra(getString(R.string.COLLETION_ITEM_KEY));
            if (collectionItem != null) {
                insertItemIntoDatabase(collectionItem, getView());
                refreshFragment();
            }
        }
        if (requestCode == CollectionItemAdapter.EDIT_ITEM_CODE
                && resultCode == RESULT_OK
                && data != null) {
            CollectionItem collectionItem = data.getParcelableExtra(getString(R.string.COLLETION_ITEM_KEY));
            if (collectionItem != null) {
                int position = data.getIntExtra(getString(R.string.POSITION_KEY), -1);
                if (position != -1) {
                    updateItemFromDatabase(collectionItem, getView());
                }
                refreshFragment();
            }
        }
        if (requestCode == CollectionItemAdapter.EDIT_ITEM_CODE
                && resultCode == CollectionItemAdapter.RESULT_DELETE && data != null) {
            CollectionItem collectionItem = data.getParcelableExtra(getString(R.string.COLLETION_ITEM_KEY));
            if (collectionItem != null) {
                deleteItemFromDatabase(collectionItem, getView());
                collectionItems.remove(collectionItem);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteItemFromDatabase(CollectionItem collectionItem, View view) {
        new CollectionItemService.Delete(view.getContext()) {
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                refreshFragment();
            }
        }.execute(collectionItem);
        refreshFragment();
    }

    private void refreshFragment() {
        recyclerView.getAdapter().notifyDataSetChanged();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private ArrayList<CollectionItem> prepareData() {
        ArrayList<CollectionItem> collectionItems = new ArrayList<>();
        CollectionItem collectionItem = new CollectionItem();
        collectionItem.setName(getString(R.string.ADD_ITEM_NAME_KEY));
        collectionItems.add(collectionItem);
        collectionItems.addAll(HomeFragment.collectionItems);
        return collectionItems;
    }

    @SuppressLint("StaticFieldLeak")
    private void insertItemIntoDatabase(CollectionItem collectionItem, View view) {
        new CollectionItemService.Insert(view.getContext()) {
            @Override
            protected void onPostExecute(CollectionItem result) {
                if (result != null) {
                    refreshFragment();
                }
            }
        }.execute(collectionItem);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateItemFromDatabase(CollectionItem collectionItem, View view) {
        new CollectionItemService.Update(view.getContext()) {
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if (integer != -1) {
                    refreshFragment();
                }
            }
        }.execute(collectionItem);
    }


    @SuppressLint("StaticFieldLeak")
    private void getItemsFromDatabase(final View view) {
        new CollectionItemService.GetAll(view.getContext()) {
            @Override
            protected void onPostExecute(
                    List<CollectionItem> results) {
                if (results != null) {
                    collectionItems.clear();
                    collectionItems.addAll(results);
                    initComp(view);
                }
            }
        }.execute();
    }
}
