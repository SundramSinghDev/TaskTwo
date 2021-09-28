package com.sundram.tasktwo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.sundram.tasktwo.R;
import com.sundram.tasktwo.adapters.UserAdapter;
import com.sundram.tasktwo.databinding.ActivityMainBinding;
import com.sundram.tasktwo.model.UserDataModel;
import com.sundram.tasktwo.viewmodel.UserViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel viewModel;
    private static final String TAG = "MAIN_ACTIVITY";

    @Inject
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            getUserData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserData() {
        viewModel.getUserData(this, 1).observe(this, new Observer<List<UserDataModel>>() {
            @Override
            public void onChanged(List<UserDataModel> userDataModelList) {
                if (userDataModelList.size()>0){
                    binding.userRv.setHasFixedSize(true);
                    binding.userRv.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
                    userAdapter.setData(userDataModelList,MainActivity.this);
                    binding.userRv.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}