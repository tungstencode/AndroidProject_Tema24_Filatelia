package com.partenie.alex.filatelie.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.partenie.alex.filatelie.AddItemActivity;
import com.partenie.alex.filatelie.HomeFragment;
import com.partenie.alex.filatelie.R;

import java.util.ArrayList;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.ViewHolder> {
    private ArrayList<CollectionItem> galleryList;
    private HomeFragment context;

    public CollectionItemAdapter(HomeFragment context, ArrayList<CollectionItem> galleryList) {
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
    public void onBindViewHolder(@NonNull final CollectionItemAdapter.ViewHolder holder, final int position) {
        if (galleryList.get(position).getName() == "-1") {
            holder.title.setText("");
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.img.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.getContext(), AddItemActivity.class);
                    context.startActivityForResult(intent, 303);
                }
            });
        } else {
            holder.title.setText(galleryList.get(position).getName());
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap bitmap = BitmapFactory.decodeFile(galleryList.get(position).getImgLocation());
            holder.img.setImageBitmap(bitmap);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                    intent.putExtra("key", holder.title.getText());
                    intent.putExtra(String.valueOf(R.string.EDIT_ITEM_KEY), galleryList.get(position));
                    intent.putExtra("position",position);
                    context.startActivityForResult(intent, 202);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
