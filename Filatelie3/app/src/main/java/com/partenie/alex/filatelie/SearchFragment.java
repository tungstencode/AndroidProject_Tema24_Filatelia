package com.partenie.alex.filatelie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        simpleList = (ListView)view.findViewById(R.id.website_lsitview);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.listview, R.id.textView, countryList);
//        simpleList.setAdapter(arrayAdapter);
//        ArrayAdapter<CharSequence> adapte2r=new ArrayAdapter<>(view.getContext(),R.layout.listview,R.id.website_lsitview);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(view.getContext(), R.array.websites,
                        android.R.layout.simple_list_item_1);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        simpleList.setAdapter(adapter);

        return view;
    }
}
