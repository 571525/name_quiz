package com.hvl.dat153.namequiz3.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hvl.dat153.namequiz3.R;
import com.hvl.dat153.namequiz3.Room.DbHandler;
import com.hvl.dat153.namequiz3.Room.Item;
import com.hvl.dat153.namequiz3.Utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    private List<Item> dataset;
    private Context context;
    private DbHandler database;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(List<Item> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;

        database = DbHandler.getInstance(context);
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.database_row_template, parent, false);

        return new ItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(dataset.get(position).uglifiedName);

        Bitmap image = Utils.getFile(dataset.get(position).image_url, context);
        holder.image.setImageBitmap(image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = dataset.get(position);

                database.deleteItem(dataset.get(position));
                dataset.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataset.size());

                Toast.makeText(context, "You deleted " + item.uglifiedName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;
        public Button delete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            image = itemView.findViewById(R.id.item_image);
            delete = itemView.findViewById(R.id.delete_button);
        }
    }
}
