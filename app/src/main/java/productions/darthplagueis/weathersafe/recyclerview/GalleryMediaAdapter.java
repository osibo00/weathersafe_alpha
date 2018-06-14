package productions.darthplagueis.weathersafe.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.model.UserMedia;

import static productions.darthplagueis.weathersafe.util.ScreenDimensions.getOneFourthDimens;

public class GalleryMediaAdapter extends RecyclerView.Adapter<GalleryMediaAdapter.UMViewHolder> {

    private final LayoutInflater inflater;

    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private List<UserMedia> userMediaList;

    private boolean isMultiSelection;

    private int amountSelected;
    private int imageDimensions;

    private GalleryClickListener listener;

    public GalleryMediaAdapter(Context context, GalleryClickListener listener) {
        inflater = LayoutInflater.from(context);
        imageDimensions = getOneFourthDimens(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.usermedia_item, parent, false);
        return new UMViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UMViewHolder holder, int position) {
        holder.onBind(userMediaList.get(position));
    }

    @Override
    public int getItemCount() {
        return userMediaList != null ? userMediaList.size() : 0;
    }

    public void setUserMediaList(List<UserMedia> userMediaData) {
        userMediaList = userMediaData;
        notifyDataSetChanged();
    }

    public void enableMultiSelection() {
        isMultiSelection = true;
        notifyDataSetChanged();
    }

    public void disableMultiSelection() {
        isMultiSelection = false;
        itemStateArray.clear();
        amountSelected = 0;
        notifyDataSetChanged();
    }

    class UMViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageView;
        AppCompatCheckBox checkBox;
        UserMedia userMedia;
        TextView textView;

        UMViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_imageView);
            checkBox = itemView.findViewById(R.id.photo_checkBox);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemSelection();
        }

        @Override
        public boolean onLongClick(View v) {
            toggleMultiSelection();
            return true;
        }

        void onBind(UserMedia item) {
            userMedia = item;
            Glide.with(itemView)
                    .load(new File(item.getMediaFilePath()))
                    .apply(new RequestOptions().override(imageDimensions, imageDimensions))
                    .into(imageView);

            textView.setText(item.getMediaFileDirectory());

            displaySelectionStatus();
        }

        void itemSelection() {
            if (isMultiSelection) {
                int position = getAdapterPosition();
                boolean isItemSelected = itemStateArray.get(position);
                if (isItemSelected) {
                    checkBox.setChecked(false);
                    itemStateArray.delete(position);
                    amountSelected--;
                } else {
                    checkBox.setChecked(true);
                    itemStateArray.put(position, true);
                    amountSelected++;
                }
                listener.onMultiSelectClick(amountSelected, userMedia);
            } else {
                listener.onClick(userMedia);
            }
        }

        void toggleMultiSelection() {
            listener.onLongClick(isMultiSelection);
        }

        void displaySelectionStatus() {
            if (!isMultiSelection) {
                checkBox.setVisibility(View.GONE);
            } else {
                checkBox.setVisibility(View.VISIBLE);
                boolean isItemSelected = itemStateArray.get(getAdapterPosition(), false);
                if (!isItemSelected) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        }
    }
}
