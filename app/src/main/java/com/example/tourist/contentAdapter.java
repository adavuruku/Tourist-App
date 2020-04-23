package com.example.tourist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class contentAdapter extends RecyclerView.Adapter<contentAdapter.RecyclerHolder>{
    private LayoutInflater inflater;
    private List<myModels.contentModel> contacts;
    private String stat;
    private Context activity;
    private  OnItemClickListener mlistener;
    public contentAdapter(List<myModels.contentModel> contacts, Context context, OnItemClickListener listener){
        this.activity = context;
        this.inflater = LayoutInflater.from(context);
        this.mlistener = listener;
        this.contacts = contacts;
    }
    public interface OnItemClickListener{
        void onVisitedClick(View v, int position);
        void onFavouriteClick(View v, int position);
        void onImageClick(View v, int position);
    }
    public void setOnitemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customitems,parent,false);
        RecyclerHolder holder= new RecyclerHolder(view,mlistener);
        return holder;
    }
    int prevpos=0;
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        try {
            int g = holder.getAdapterPosition();
            myModels.contentModel contact = contacts.get(g);
            holder.title.setText(contact.getTitle());


            Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getBlobpics(), 0, contact.getBlobpics().length);
            holder.imageView.setImageBitmap(bitmap);

            if(contact.getFavourite().equals("1")){
                holder.favourite.setBackgroundResource(R.drawable.circleselected);
            }

            if(contact.getTravel().equals("1")){
                holder.visited.setBackgroundResource(R.drawable.circleselected);
            }

            if (position > prevpos) {
                AnimationUtils.animate(holder, true);
            } else {
                AnimationUtils.animate(holder, false);
            }
            prevpos = position;
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    //create the holder class
    class RecyclerHolder extends RecyclerView.ViewHolder{
        //the view items send here is from custom_row and is received here as itemView
        TextView title;
        ImageView imageView;
        ImageButton visited, favourite;
        public RecyclerHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            visited =  itemView.findViewById(R.id.visited);
            favourite =  itemView.findViewById(R.id.favourite);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onImageClick(view, position);
                        }
                    }
                }
            });
            visited.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onVisitedClick(view, position);
                        }
                    }
                }
            });

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onFavouriteClick(view, position);
                        }
                    }
                }
            });
        }
    }

    public void setFilter(ArrayList<myModels.contentModel> newList){
        contacts = new ArrayList<>();
        contacts.addAll(newList);
        notifyDataSetChanged();
    }

}
