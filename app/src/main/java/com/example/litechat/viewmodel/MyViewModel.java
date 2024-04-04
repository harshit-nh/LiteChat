package com.example.litechat.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.litechat.Repository.Repository;
import com.example.litechat.model.ChatGroup;
import com.example.litechat.model.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;
    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    //Auth
    public void signUpAnonymousUser(){
        Context c = this.getApplication();
        repository.firebaseAnonymousAuth(c);
    }

    public String getCurrentUserId(){
        return repository.getCurrentUserId();
    }

    public void signOut(){
        repository.signOut();
    }


    //Getting Chat Groups
    public MutableLiveData<List<ChatGroup>> groupList(){
        return repository.getChatGroupMutableLiveData();
    }

    public void createChatGroup(String groupName){
        repository.createNewChatGroup(groupName);
    }

    //Chat messages
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName){
        return repository.getMessageLiveData(groupName);
    }


    //Sending messages
    public void sendMessage(String msg, String chatGroup){
        repository.sendMessage(msg,chatGroup);
    }

}
