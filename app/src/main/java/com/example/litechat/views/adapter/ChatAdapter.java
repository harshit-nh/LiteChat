package com.example.litechat.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litechat.BR;
import com.example.litechat.R;
import com.example.litechat.databinding.RowLayoutBinding;
import com.example.litechat.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatMessage> chatMessageList;
    Context context;

    public ChatAdapter(List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_layout,parent,false);

        RowLayoutBinding binding = DataBindingUtil.bind(view);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.chatMessage, chatMessageList.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private RowLayoutBinding binding;


        public MyViewHolder(RowLayoutBinding binding) {
            super(binding.getRoot());
            setBinding(binding);
        }

        public RowLayoutBinding getBinding() {
            return binding;
        }

        public void setBinding(RowLayoutBinding binding) {
            this.binding = binding;
        }
    }
}
