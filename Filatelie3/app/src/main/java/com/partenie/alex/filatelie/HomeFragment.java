package com.partenie.alex.filatelie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.partenie.alex.filatelie.util.CollectionItem;
import com.partenie.alex.filatelie.util.CollectionItemAdapter;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    public static ArrayList<CollectionItem> collectionItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_gallery);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(getString(R.string.SETTINGS_KEY), Context.MODE_PRIVATE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), sharedPreferences.getInt(getString(R.string.ITEM_PER_COLLUMN_KEY),1)+1);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CollectionItem> createLists = prepareData(view);
        CollectionItemAdapter adapter = new CollectionItemAdapter(this, createLists);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CollectionItemAdapter.ADD_ITEM_CODE
                && resultCode == RESULT_OK
                && data != null) {
            CollectionItem collectionItem = data.getParcelableExtra(getString(R.string.COLLETION_ITEM_KEY));
            if (collectionItem != null) {
                collectionItems.add(collectionItem);
                recyclerView.getAdapter().notifyDataSetChanged();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            }
        }
        if (requestCode == CollectionItemAdapter.EDIT_ITEM_CODE
                && resultCode == RESULT_OK
                && data != null) {
            CollectionItem collectionItem = data.getParcelableExtra(getString(R.string.COLLETION_ITEM_KEY));
            if (collectionItem != null) {
                int position=data.getIntExtra(getString(R.string.POSITION_KEY),-1);
                if(position!=-1){
                    collectionItems.remove(position-1);
                    collectionItems.add(collectionItem);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            }
        }
    }

    private ArrayList<CollectionItem> prepareData(View view) {
        ArrayList<CollectionItem> collectionItems = new ArrayList<>();
        CollectionItem collectionItem = new CollectionItem();
        collectionItem.setName(getString(R.string.ADD_ITEM_NAME_KEY));
        collectionItems.add(collectionItem);
        collectionItems.addAll(HomeFragment.collectionItems);
        return collectionItems;
    }
}
