package com.partenie.alex.filatelie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.partenie.alex.filatelie.R;
import com.partenie.alex.filatelie.util.CollectionItem;
import com.partenie.alex.filatelie.util.CollectionItemAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    Button button;
    RecyclerView recyclerView;

    private final String image_titles[] = {
            "-1",
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
            "Img6",
            "Img7",
            "Img8",
            "Img9",
            "Img10",
            "Img11",
            "Img12",
            "Img13",
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=view.findViewById(R.id.recycler_view_gallery);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CollectionItem> createLists = prepareData();
        CollectionItemAdapter adapter = new CollectionItemAdapter(getContext(), createLists);
        recyclerView.setAdapter(adapter);



//        button=view.findViewById(R.id.add_itembut);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(),"lol",Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(getContext(),AddItemActivity.class);
//                startActivityForResult(intent,200);
//            }
//        });
        return view;
    }


    private ArrayList<CollectionItem> prepareData(){

        ArrayList<CollectionItem> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            CollectionItem collectionItem = new CollectionItem();
            collectionItem.setName(image_titles[i]);
//            collectionItem.setImgLocation(image_ids[i]);
            theimage.add(collectionItem);
        }
        return theimage;
    }
}
