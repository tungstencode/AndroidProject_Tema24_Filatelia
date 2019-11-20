package com.partenie.alex.filatelie.util;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.partenie.alex.filatelie.R;

import java.util.ArrayList;

public class CollectionItemAdapter extends  RecyclerView.Adapter<CollectionItemAdapter.ViewHolder> {
    private ArrayList<CollectionItem> galleryList;
    private Context context;

    public CollectionItemAdapter(Context context, ArrayList<CollectionItem> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CollectionItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionItemAdapter.ViewHolder holder, int position) {
        holder.title.setText(galleryList.get(position).getName());
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.img.setImageResource(R.drawable.ic_settings_black_24dp);  //(galleryList.get(position).getImgLocation())
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
