package com.example.litechat.Repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.litechat.model.ChatGroup;
import com.example.litechat.model.ChatMessage;
import com.example.litechat.views.GroupsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    //Repository acts as a bridge between the ViewModel and the Data Source

    MutableLiveData<List<ChatGroup>> chatGroupMutableLiveData;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference groupReference;

    MutableLiveData<List<ChatMessage>> messageLiveData;

    public Repository() {
        this.chatGroupMutableLiveData = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        messageLiveData = new MutableLiveData<>();
    }

    public void firebaseAnonymousAuth(Context context){

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(context, GroupsActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                });

    }

    //getting current user id
    public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    //SignOut Functionality
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    //Getting chat groups available from realtime db
    public MutableLiveData<List<ChatGroup>> getChatGroupMutableLiveData() {
        List<ChatGroup> groupList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ChatGroup group = new ChatGroup(dataSnapshot.getKey());
                    groupList.add(group);
                }

                chatGroupMutableLiveData.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return chatGroupMutableLiveData;
    }

    //Creating a new Group
    public void createNewChatGroup(String groupName){
        reference.child(groupName).setValue(groupName);
    }


    //Getting live message data
    public MutableLiveData<List<ChatMessage>> getMessageLiveData(String groupName) {

        groupReference = database.getReference().child(groupName);

        List<ChatMessage> messageList = new ArrayList<>();
        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageList.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }
                messageLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return messageLiveData;
    }


    public void sendMessage(String messageTxt, String chatGroup){
        DatabaseReference ref =
                database.getReference(chatGroup);

        String time = String.valueOf(System.currentTimeMillis());

        if(!messageTxt.trim().isEmpty()){
            ChatMessage msg = new ChatMessage(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    messageTxt,
                    time
            );

            String randomKey = ref.push().getKey();
            assert randomKey != null;
            ref.child(randomKey).setValue(msg);
        }
    }
}
