package com.example.chatapp.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Messages.Messages;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.chatapp.Activity.ChatActivity.reciverimage;
import static com.example.chatapp.Activity.ChatActivity.senderiamge;

public class MessagesAdaptor extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;

    public MessagesAdaptor(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND)
        {
            View v = LayoutInflater.from(context).inflate(R.layout.sender_chat,parent,false);
            return new SenderViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(context).inflate(R.layout.reciver_chat,parent,false);
            return new SenderViewHolder(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Messages messages = messagesArrayList.get(position);
            if (holder.getClass()==SenderViewHolder.class)
            {
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.textView.setText(messages.getMessage());
                Picasso.get().load(senderiamge).into(viewHolder.circleImageView);

            }
            else {

                ReciverViewHolder viewHolder = (ReciverViewHolder) holder;
                viewHolder.textView.setText(messages.getMessage());
                Picasso.get().load(reciverimage).into(viewHolder.circleImageView);

            }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.senderiteamview);
            textView = itemView.findViewById(R.id.senderTextview);
        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;
        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.reciveriteamview);
            textView = itemView.findViewById(R.id.reciverTextview);
        }
    }

}
