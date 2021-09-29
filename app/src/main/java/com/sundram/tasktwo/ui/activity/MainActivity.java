package com.sundram.tasktwo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sundram.tasktwo.R;
import com.sundram.tasktwo.adapters.UserAdapter;
import com.sundram.tasktwo.databinding.ActivityMainBinding;
import com.sundram.tasktwo.databinding.FilterBottomSheetBinding;
import com.sundram.tasktwo.interfaces.GenderFilter;
import com.sundram.tasktwo.model.GenderFilterModel;
import com.sundram.tasktwo.model.Response;
import com.sundram.tasktwo.model.UserDataModel;
import com.sundram.tasktwo.model.UserRoomDBModel;
import com.sundram.tasktwo.utils.ConnectionUtils;
import com.sundram.tasktwo.utils.ConstantUtils;
import com.sundram.tasktwo.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel viewModel;
    private static final String TAG = "MAIN_ACTIVITY";
     private SearchView searchView;
    private List<UserDataModel> data;
    List<UserDataModel> dataModelList = new ArrayList<>();
    @Inject
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            binding.swipeRefresh.setOnRefreshListener(() -> {
                getUserData();
                binding.swipeRefresh.setRefreshing(false);
            });
            getUserData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserData() {
        try {
            if (ConnectionUtils.checkConnectivity(this)) {
                viewModel.getUserData(this, 1).observe(this, this::handleResponse);
            } else {
                viewModel.getUserDataFromRoomDB(1).observe(this, this::handleLocalResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLocalResponse(UserRoomDBModel userRoomDBModel) {
        if (userRoomDBModel.getResponse().getData().size() > 0) {
            data = new ArrayList<>(userRoomDBModel.getResponse().getData());
            setRecyclerViewAndAdapter(userRoomDBModel.getResponse().getData());
        }
    }

    private void handleResponse(Response response) {
        //insert data into room db
        viewModel.insertUserDataIntoRoomDB(new UserRoomDBModel(1, response));
        List<UserDataModel> userDataModelList = new ArrayList<>(response.getData());
        if (userDataModelList.size() > 0) {
            data = new ArrayList<>(userDataModelList);
            setRecyclerViewAndAdapter(userDataModelList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        // Associate searchable configuration with the SearchView
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setIconified(false);
        searchView.setQueryHint("Enter User Name..");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            showBottomSheetDialog();
        }
        if (item.getItemId() == R.id.refresh) {
            OnRefresh();
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnRefresh() {
        getUserData();
    }


    private void showBottomSheetDialog() {
        try {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            FilterBottomSheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(MainActivity.this), R.layout.filter_bottom_sheet, null, false);
            bottomSheetDialog.setContentView(binding.getRoot());
            binding.radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_female_btn) {
                    filter(getResources().getString(R.string.female));
                } else {
                    filter(getResources().getString(R.string.male));
                }
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filter(String keyword) {
       if (ConnectionUtils.checkConnectivity(MainActivity.this)){
           dataModelList.clear();
           if (keyword == null || keyword.length() == 0) {
               dataModelList.addAll(data);
               return;
           }
           for (UserDataModel dataModel : data) {
               if (dataModel.getName().toLowerCase().contains(keyword.toLowerCase()) || dataModel.getGender().equals(keyword.toLowerCase()) == true) {
                   dataModelList.add(dataModel);
               }
           }
           setRecyclerViewAndAdapter(dataModelList);
       }else{
           Toast.makeText(MainActivity.this, ConstantUtils.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
       }

    }

    private void setRecyclerViewAndAdapter(List<UserDataModel> userDataModelList) {
        binding.userRv.setHasFixedSize(true);
        binding.userRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        userAdapter.setData(userDataModelList, MainActivity.this);
        binding.userRv.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

}