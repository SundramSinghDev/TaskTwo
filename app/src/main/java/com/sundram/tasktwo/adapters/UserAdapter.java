package com.sundram.tasktwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sundram.tasktwo.R;
import com.sundram.tasktwo.databinding.SingleUserItemViewBinding;
import com.sundram.tasktwo.model.UserDataModel;

import java.util.List;

import javax.inject.Inject;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public List<UserDataModel> userDataModelList;
    private Context context;
    private SingleUserItemViewBinding binding;
    private static final String TAG = "ADAPTER";

    @Inject
    public UserAdapter() { }

    public void setData(List<UserDataModel> userDataModelList, Context context) {
        this.userDataModelList = userDataModelList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.single_user_item_view, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            binding.setUserData(userDataModelList.get(position));
            String name[] = userDataModelList.get(position).getName().split("");
            binding.setSetAvatar(name[0].charAt(0) + "." + name[0].charAt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return userDataModelList.size() > 0 ? userDataModelList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleUserItemViewBinding singleUserItemViewBinding;

        public ViewHolder(@NonNull SingleUserItemViewBinding itemView) {
            super(itemView.getRoot());
            singleUserItemViewBinding = itemView;
        }
    }
}
