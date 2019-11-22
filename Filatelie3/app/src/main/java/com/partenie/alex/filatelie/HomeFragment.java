package com.partenie.alex.filatelie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    Button button;
    RecyclerView recyclerView;
    ArrayList<String> paths = new ArrayList<String>();
    File[] listFile;

//    private final String image_titles[] = {
//            "-1",
//            "Img1",
//            "Img2",
//            "Img3",
//            "Img4",
//            "Img5",
//            "Img6",
//            "Img7",
//            "Img8",
//            "Img9",
//            "Img10",
//            "Img11",
//            "Img12",
//            "Img13",
//    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_gallery);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CollectionItem> createLists = prepareData(view);
        CollectionItemAdapter adapter = new CollectionItemAdapter(getContext(), createLists);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private ArrayList<CollectionItem> prepareData(View view) {

        getFromSdcard(view);
        ArrayList<CollectionItem> theimages = new ArrayList<>();

        CollectionItem collectionItem = new CollectionItem();
        collectionItem.setName("-1");
//            collectionItem.setImgLocation(image_ids[i]);
        theimages.add(collectionItem);


        for (int i = 0; i < paths.size(); i++) {
            collectionItem = new CollectionItem();

            collectionItem.setName(paths.get(i));
            collectionItem.setImgLocation(paths.get(i));
            theimages.add(collectionItem);
        }
        return theimages;
    }


    public void getFromSdcard(View view) {

        String myfolder = Environment.getExternalStorageDirectory() + "/CollectionPhotos";
        File f = new File(myfolder);
        if (!f.exists()) {
            if (!f.mkdir()) {
                Toast.makeText(view.getContext(), myfolder + " can't be created.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), myfolder + " can be created.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(view.getContext(), myfolder + " already exits.", Toast.LENGTH_SHORT).show();
            try{
                listFile = f.listFiles();

                if(listFile!=null){
                    for (int i = 0; i < listFile.length; i++) {
                        Toast.makeText(view.getContext(),listFile[i].getAbsolutePath(),Toast.LENGTH_SHORT);
                        paths.add(listFile[i].getAbsolutePath());
                    }
                }else{
                    Toast.makeText(view.getContext(),"list file is null",Toast.LENGTH_SHORT);
                }
            }catch (Exception e){}
        }
    }
}
