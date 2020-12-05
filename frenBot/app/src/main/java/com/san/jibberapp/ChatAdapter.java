package com.san.jibberapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int BOT_MESSAGE = 1;
    private static final int USER_MESSAGE = 2 ;
    private LayoutInflater inflater;
    private List<Chat> dataModelArrayList;
    private OnNoteList onNoteList;


    public ChatAdapter(Context ctx, List<Chat> dataModelArrayList, OnNoteList onNoteList){

        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
        this.onNoteList = onNoteList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("viewType = " + viewType);
        if(viewType == BOT_MESSAGE){
            View view = inflater.inflate(R.layout.bot_msg_layout, parent, false);
            BotViewHolder holder = new BotViewHolder(view,onNoteList);
            return holder;
        }
        else {
            View view = inflater.inflate(R.layout.user_msg_layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view,onNoteList);
            return holder;
        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == BOT_MESSAGE){
            ((BotViewHolder)holder).message.setText(dataModelArrayList.get(position).getMessage());

        }
        else{
            ((MyViewHolder)holder).message.setText(dataModelArrayList.get(position).getMessage());


        }



    }

    @Override
    public int getItemCount() {
        //.println("dataModelArrayList.size() = " + dataModelArrayList.size());
        if(dataModelArrayList == null){
            return 0;
        }
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener  {
        TextView message;

        OnNoteList onNoteList;

        public MyViewHolder(@NonNull View itemView , OnNoteList onNoteList) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_msg);



            this.onNoteList = onNoteList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);




        }

        @Override
        public void onClick(View v) {
            onNoteList.OnnoteClick(dataModelArrayList.get(getAdapterPosition()));

        }




        @Override
        public boolean onLongClick(View v) {
            onNoteList.OnLongClick(dataModelArrayList.get(getAdapterPosition()));
            return true;
        }
    }

    public class BotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener  {
        TextView message;

        OnNoteList onNoteList;

        public BotViewHolder(@NonNull View itemView , OnNoteList onNoteList) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_msg);


            this.onNoteList = onNoteList;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onNoteList.OnnoteClick(dataModelArrayList.get(getAdapterPosition()));

        }




        @Override
        public boolean onLongClick(View v) {
            onNoteList.OnLongClick(dataModelArrayList.get(getAdapterPosition()));
            return true;
        }
    }
    public interface OnNoteList {
        void OnnoteClick(Chat userClass);
        void OnLongClick(Chat downloadObject);


    }

    public void filteredlist(ArrayList<Chat> filterlist){
        dataModelArrayList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("position = " + position);
        if(dataModelArrayList.get(position).isBotMessage()){
            return BOT_MESSAGE;
        }
        else{
            return USER_MESSAGE;
        }
        //return super.getItemViewType(position);
    }
}


