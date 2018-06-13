package productions.darthplagueis.weathersafe.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.util.ScreenDimensions;

public class UserMediaAdapter extends RecyclerView.Adapter<UserMediaAdapter.UserMediaViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private List<UserMedia> userMediaList;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private boolean isMultiSelection;
    private int itemsSelected;
    private OnItemsSelectedListener itemsSelectedListener;

    public UserMediaAdapter(Context context, OnItemsSelectedListener itemsSelectedListener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.itemsSelectedListener = itemsSelectedListener;
    }

    @NonNull
    @Override
    public UserMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.usermedia_item, parent, false);
        return new UserMediaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMediaViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return userMediaList != null ? userMediaList.size() : 0;
    }

    public void setUserMediaList(List<UserMedia> mUserMediaList) {
        userMediaList = mUserMediaList;
        notifyDataSetChanged();
    }

    public List<UserMedia> getUserMedia() {
        List<Integer> positions = getSelectedItems();
        List<UserMedia> items = new ArrayList<>(positions.size());
        for (int i = 0; i < items.size(); i++) {
            items.add(userMediaList.get(positions.get(i)));
        }
        return items;
    }

    public void quitMultiSelection() {
        isMultiSelection = false;
        itemsSelectedListener.multiSelectionEnabled(false);
        itemsSelected = 0;
        itemStateArray.clear();
        notifyDataSetChanged();
        Log.d("Adapter", "quitMultiSelection: quit");
    }

    private List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(itemStateArray.size());
        for (int i = 0; i < itemStateArray.size(); i++) {
            items.add(itemStateArray.keyAt(i));
        }
        return items;
    }

    class UserMediaViewHolder extends RecyclerView.ViewHolder implements
            View.OnLongClickListener, View.OnClickListener {

        private ImageView imageView;
        private AppCompatCheckBox checkBox;

        UserMediaViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_imageView);
            checkBox = itemView.findViewById(R.id.photo_checkBox);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (!isMultiSelection) {
                isMultiSelection = true;
                itemsSelectedListener.multiSelectionEnabled(true);
                checkBox.setChecked(true);
                itemStateArray.put(getAdapterPosition(), true);
                notifyDataSetChanged();
                itemsSelected++;
                itemsSelectedListener.amountSelected(itemsSelected);
                Log.d("Adapter", "quitMultiSelection: onlongclick");
            } else {
                quitMultiSelection();
                Log.d("Adapter", "quitMultiSelection: onlongclick else");
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            if (isMultiSelection) {
                int position = getAdapterPosition();
                boolean isItemSelected = itemStateArray.get(position);
                if (isItemSelected) {
                    checkBox.setChecked(false);
                    itemStateArray.delete(position);
                    itemsSelected--;
                    itemsSelectedListener.amountSelected(itemsSelected);
                } else {
                    checkBox.setChecked(true);
                    itemStateArray.put(position, true);
                    itemsSelected++;
                    itemsSelectedListener.amountSelected(itemsSelected);
                }
            }
        }

        private void onBind(int position) {
            UserMedia item = userMediaList.get(position);
            Glide.with(itemView)
                    .load(new File(item.getMediaFilePath()))
                    .apply(new RequestOptions().override
                            (ScreenDimensions.getOneFourthDimens(context),
                            ScreenDimensions.getScreenHeight(context)))
                    .into(imageView);

            if (!isMultiSelection) {
                checkBox.setVisibility(View.GONE);
            } else {
                checkBox.setVisibility(View.VISIBLE);

                if (!itemStateArray.get(position, false)) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        }
    }

    public interface OnItemsSelectedListener {
        void multiSelectionEnabled(boolean enabled);

        void amountSelected(int itemCount);
    }
}
