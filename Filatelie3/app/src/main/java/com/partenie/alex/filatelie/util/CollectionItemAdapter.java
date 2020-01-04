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
import com.partenie.alex.filatelie.database.model.CollectionItem;

import java.util.ArrayList;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.ViewHolder> {
    private ArrayList<CollectionItem> galleryList;
    private HomeFragment context;
    public static final int ADD_ITEM_CODE = 303;
    public static final int EDIT_ITEM_CODE = 202;
    public static final int RESULT_DELETE = 204;



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
        if (galleryList.get(position).getName() == context.getString(R.string.ADD_ITEM_NAME_KEY)) {
            holder.title.setText(R.string.ADD_ITEM_STRING);
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.img.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.getContext(), AddItemActivity.class);
                    context.startActivityForResult(intent, ADD_ITEM_CODE);
                }
            });
        } else {
            holder.title.setText(galleryList.get(position).getName());
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(galleryList.get(position).getImgLocation(), options);
            } catch (Exception e) {

            }
            holder.img.setImageBitmap(bitmap);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddItemActivity.class);
                    intent.putExtra(context.getString(R.string.COLLECTION_ITEM_EDIT), holder.title.getText());
                    intent.putExtra(String.valueOf(R.string.EDIT_ITEM_KEY), galleryList.get(position));
                    intent.putExtra(context.getString(R.string.POSITION_KEY), position);
                    context.startActivityForResult(intent, EDIT_ITEM_CODE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;

        ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
        }
    }
}
