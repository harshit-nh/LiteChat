package com.example.litechat.views;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.litechat.R;
import com.example.litechat.databinding.ActivityGroupsBinding;
import com.example.litechat.model.ChatGroup;
import com.example.litechat.viewmodel.MyViewModel;
import com.example.litechat.views.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroupArrayList;

    private RecyclerView recyclerView;
    private GroupAdapter groupAdapter;
    private ActivityGroupsBinding binding;
    private MyViewModel myViewModel;

    private Dialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_groups);

        //Define the viewModel
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        //Recycler view with data binding
        recyclerView = binding.groupRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //Set up an observer to listen for changes in the "Live Data" object
        myViewModel.groupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                //The updated data is received as "chatGroups" parameter in onChanged()
                chatGroupArrayList = new ArrayList<>();
                chatGroupArrayList.addAll(chatGroups);

                groupAdapter= new GroupAdapter(chatGroupArrayList);
                recyclerView.setAdapter(groupAdapter);
                groupAdapter.notifyDataSetChanged();
            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });




        //Exit Confirmation dialog
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };

        getOnBackPressedDispatcher().addCallback(this,callback);


    }

    public void showDialog(){
        dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_layout, null);

        dialog.setContentView(view);
        dialog.show();

        Button submit = view.findViewById(R.id.buttonDialog);
        EditText edtTxt = view.findViewById(R.id.editTextDialog);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = edtTxt.getText().toString();
                Toast.makeText(GroupsActivity.this,
                        "Your Chat Group: "+groupName,Toast.LENGTH_SHORT).show();

                myViewModel.createChatGroup(groupName);

                dialog.dismiss();

            }
        });
    }


    //Exit confirmation dialog function
    private void showExitConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}