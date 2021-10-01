package com.sundram.tasktwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sundram.tasktwo.R;
import com.sundram.tasktwo.databinding.SingleUserItemViewBinding;
import com.sundram.tasktwo.model.UserDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    public List<UserDataModel> userDataModelList;
    public List<UserDataModel> filteredList;
    private Context context;
    private SingleUserItemViewBinding binding;
    private static final String TAG = "ADAPTER";

    @Inject
    public UserAdapter() {
    }

    public void setData(List<UserDataModel> userDataModelList, Context context) {
        this.userDataModelList = userDataModelList;
        this.filteredList = userDataModelList;
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
            binding.setUserData(filteredList.get(position));
            String name[] = filteredList.get(position).getName().split("");
            binding.setSetAvatar(name[0].charAt(0) + "." + name[0].charAt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return filteredList.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filteredList.size() > 0 ? filteredList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                List<UserDataModel> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered = userDataModelList;
                } else {
                    for (UserDataModel userDataModel : userDataModelList) {
                        if (userDataModel.getGender().toLowerCase().equals(charSequence)) {
                            filtered.add(userDataModel);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = filtered.size();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<UserDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleUserItemViewBinding singleUserItemViewBinding;

        public ViewHolder(@NonNull SingleUserItemViewBinding itemView) {
            super(itemView.getRoot());
            singleUserItemViewBinding = itemView;
        }
    }


}
