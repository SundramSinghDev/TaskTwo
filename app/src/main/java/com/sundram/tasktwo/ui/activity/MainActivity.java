package com.sundram.tasktwo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sundram.tasktwo.R;
import com.sundram.tasktwo.adapters.UserAdapter;
import com.sundram.tasktwo.databinding.ActivityMainBinding;
import com.sundram.tasktwo.databinding.FilterBottomSheetBinding;
import com.sundram.tasktwo.model.Response;
import com.sundram.tasktwo.model.UserDataModel;
import com.sundram.tasktwo.model.UserRoomDBModel;
import com.sundram.tasktwo.utils.ConnectionUtils;
import com.sundram.tasktwo.utils.ConstantUtils;
import com.sundram.tasktwo.utils.SharedPrefUtils;
import com.sundram.tasktwo.utils.WrapContentLinearLayoutManager;
import com.sundram.tasktwo.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private Integer TOTAL_PAGE_NO;
    private Integer PAGE_NO = 1;
    private boolean isScroll = false;
    private boolean isFilterCalled = false;

    @Inject
    UserAdapter userAdapter;

    //    LinearLayoutManager linearLayoutManager;
    WrapContentLinearLayoutManager linearLayoutManager;
    int scroll_pos = 0;
    int isMaleSelected = 0;
    int isFeMaleSelected = 0;
    boolean isGenderSelected = false;
    boolean isDataFetchFromLocal = false;
    @Inject
    SharedPrefUtils sharedPrefUtils;
    @Inject
    ConnectionUtils connectionUtils;
    BottomSheetDialog bottomSheetDialog;
    FilterBottomSheetBinding filterBottomSheetBinding;
    Response responseModel;
    Iterator iterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
            sharedPrefUtils.init(MainActivity.this);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            //this is for sending context to view model
            viewModel.init(MainActivity.this);
            connectionUtils.init(MainActivity.this);
            bottomSheetDialog = new BottomSheetDialog(this);
            filterBottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(MainActivity.this), R.layout.filter_bottom_sheet, null, false);

            binding.userRv.setHasFixedSize(true);
            linearLayoutManager = new WrapContentLinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
//            linearLayoutManager = new LinearLayoutManager(MainActivity.this);

            RecyclerView.ItemAnimator animator = binding.userRv.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            binding.userRv.setItemAnimator(null);
            //TODO
            //NEED TO RESET THE CATEGORY FILTER ON SWIPE REFRESH AND ON REFRESH

            binding.swipeRefresh.setOnRefreshListener(() -> {
                PAGE_NO = 1;
                isScroll = false;
                isFilterCalled = false;
                clearGenderFilter();
                binding.swipeRefresh.setRefreshing(false);
                getUserResponseData(1);
            });

            getUserResponseData(1);
            userAdapter.setHasStableIds(true);
            binding.userRv.setAdapter(userAdapter);
            applyPaginationOnRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserResponseData(Integer page_no) {
        try {
            if (connectionUtils.checkConnectivity()) {
                Log.i(TAG, "getUserResponseData: NET");
                isDataFetchFromLocal = false;
                viewModel.getUserData(this, page_no).observe(this, this::handleResponse);
                return;
            } else {
                isDataFetchFromLocal = true;
                Log.i(TAG, "getUserResponseLOCALData: else NO NET");
                viewModel.getUserDataFromRoomDB(1).observe(this, this::handleLocalResponse);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLocalResponse(UserRoomDBModel userRoomDBModel) {
        try {
            TOTAL_PAGE_NO = userRoomDBModel.getResponse().getMeta().getPagination().getPages();
            binding.swipeRefresh.setEnabled(false);
            if (userRoomDBModel.getResponse().getData().size() > 0 && userRoomDBModel.getResponse().getData() != null) {
                Log.i(TAG, "handleLocalResponse: ");
                data = new ArrayList<>(userRoomDBModel.getResponse().getData());
                setRecyclerViewAndAdapter(userRoomDBModel.getResponse().getData());
            } else {
                Log.i(TAG, "handleLocalResponse: EMPTY OR NULL");
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(Response response) {
        TOTAL_PAGE_NO = response.getMeta().getPagination().getPages();
        PAGE_NO = response.getMeta().getPagination().getPage();
        PAGE_NO++;
        responseModel = response;
        binding.swipeRefresh.setEnabled(true);
        List<UserDataModel> userDataModelList = new ArrayList<>(response.getData());
        if (userDataModelList.size() > 0 && userDataModelList != null) {
            Log.i(TAG, "handleResponse: ");
            data = new ArrayList<>(userDataModelList);
            setRecyclerViewAndAdapter(userDataModelList);
        } else {
            Log.i(TAG, "handleResponse: NO DATA");
        }
        return;
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
        menu.findItem(R.id.search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                isFilterCalled = false;
                clearGenderFilter();
                PAGE_NO = 1;
                binding.swipeRefresh.setEnabled(true);
                getUserResponseData(1);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    filter("");
                }
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
        PAGE_NO = 1;
        isScroll = false;
        isFilterCalled = false;
        clearGenderFilter();
        binding.swipeRefresh.setRefreshing(false);
        getUserResponseData(1);
    }

    private void showBottomSheetDialog() {
        try {
            bottomSheetDialog.setContentView(filterBottomSheetBinding.getRoot());
            bottomSheetDialog.show();
            if (isFeMaleSelected == 1) {
                filterBottomSheetBinding.radioFemaleBtn.setChecked(true);
            }
            if (isMaleSelected == 1) {
                isGenderSelected = false;
                filterBottomSheetBinding.radioMaleBtn.setChecked(true);
            }

            filterBottomSheetBinding.clearFilterTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearGenderFilter();
                    PAGE_NO = 1;
                    getUserResponseData(1);

                }
            });
            filterBottomSheetBinding.radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_female_btn) {
                    filter(getResources().getString(R.string.female));
                    isFeMaleSelected = 1;
                    isMaleSelected = 0;
                    isFilterCalled = true;
                    sharedPrefUtils.saveSelectedGender(getResources().getString(R.string.female));
                } else {
                    isMaleSelected = 1;
                    isFeMaleSelected = 0;
                    isFilterCalled = true;
                    filterBottomSheetBinding.radioMaleBtn.setChecked(true);
                    filter(getResources().getString(R.string.male));
                    sharedPrefUtils.saveSelectedGender(getResources().getString(R.string.male));
                }
                bottomSheetDialog.dismiss();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filter(String keyword) {
        isFilterCalled = true;
        binding.swipeRefresh.setEnabled(false);
        dataModelList.clear();
        if (keyword == null || keyword.length() == 0) {
            setRecyclerViewAndAdapter(data);
            return;
        }
        for (UserDataModel dataModel : data) {
            if (dataModel.getName().toLowerCase().contains(keyword.toLowerCase()) || dataModel.getGender().equals(keyword.toLowerCase()) == true) {
                dataModelList.add(dataModel);
            }
        }
        setRecyclerViewAndAdapter(dataModelList);
    }

    private void setRecyclerViewAndAdapter(List<UserDataModel> userDataModelLists) {
        try {
            binding.userRv.setLayoutManager(linearLayoutManager);
            if (isScroll) {
                Log.i(TAG, "setRecyclerViewAndAdapter: IN SCROLL");
                iterator = userDataModelLists.iterator();
                while (iterator.hasNext()) {
                    userAdapter.userDataModelList.add((UserDataModel) iterator.next());
                }
                userAdapter.setData(userAdapter.userDataModelList, MainActivity.this);
                binding.userRv.scrollToPosition(scroll_pos - 1);
            } else {
                Log.i(TAG, "setRecyclerViewAndAdapter: OUT SCROLL");
                userAdapter.setData(userDataModelLists, MainActivity.this);
            }
            binding.userRv.getRecycledViewPool().clear();
            userAdapter.notifyDataSetChanged();
            Log.i(TAG, "setRecyclerViewAndAdapter: isFilterCalled " + isFilterCalled + " isDataFetchFromLocal " + isDataFetchFromLocal);
            if (!isFilterCalled) {
                if (!isDataFetchFromLocal) {
                    Log.i(TAG, "isDataFetchFromLocal: " + isDataFetchFromLocal);
                    insertUserDataIntoRoomDB();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private void applyPaginationOnRecyclerView() {
        binding.userRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.i(TAG, "onScrollStateChanged: isFilterCalled " + isFilterCalled + "\n " + "isDataFetchFromLocal " + isDataFetchFromLocal);
                    if (!isDataFetchFromLocal) {
                        if (!isFilterCalled) {
                            if (PAGE_NO <= TOTAL_PAGE_NO) {
                                isScroll = true;
                                Log.i(TAG, "onScrollStateChanged: OK");
                                scroll_pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                                getUserResponseData(PAGE_NO);
                                return;
                            } else {
                                isScroll = false;
                                Toast.makeText(MainActivity.this, "No More Data Available..", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                } else {
                    isScroll = false;
                }
                return;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                return;
            }
        });
        return;
    }

    private void insertUserDataIntoRoomDB() {
        Response response = new Response();
        response.setData(userAdapter.userDataModelList);
        response.setCode(responseModel.getCode());
        response.setMeta(responseModel.getMeta());
        Log.i(TAG, "insertUserDataIntoRoomDB: DATA INSERTED INTO ROOM DB");
        //insert data into room db
        viewModel.insertUserDataIntoRoomDB(new UserRoomDBModel(1, response));
    }

    private void clearGenderFilter() {
        filterBottomSheetBinding.radioFemaleBtn.setChecked(false);
        filterBottomSheetBinding.radioMaleBtn.setChecked(false);

        isFeMaleSelected = 0;
        isMaleSelected = 0;

        isFilterCalled = false;
        binding.swipeRefresh.setEnabled(true);
    }
}