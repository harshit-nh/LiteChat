package com.example.litechat.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.litechat.R;
import com.example.litechat.databinding.ActivityChatBinding;
import com.example.litechat.model.ChatMessage;
import com.example.litechat.viewmodel.MyViewModel;
import com.example.litechat.views.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MyViewModel viewModel;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    private List<ChatMessage> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        //RecyclerView with DataBinding

        recyclerView = binding.recyclerViewMessage;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //Get the group name from the clicked item in the GroupsActivity
        String groupName = getIntent().getStringExtra("GROUP_NAME");

        viewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                chatMessageList= new ArrayList<>();
                chatMessageList.addAll(chatMessages);

                chatAdapter = new ChatAdapter(chatMessageList,getApplicationContext());

                recyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();

                //Automatically scroll recyclerview to the latest message position
                int latestPosition = chatAdapter.getItemCount() - 1;
                if(latestPosition>0){
                    recyclerView.smoothScrollToPosition(latestPosition);
                }
            }
        });

        binding.setVModel(viewModel);

        binding.setVModel(viewModel);

        binding.txtSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = binding.chatMessageEditTxt.getText().toString();
                viewModel.sendMessage(msg, groupName);

                binding.chatMessageEditTxt.getText().clear();
            }
        });

    }
}