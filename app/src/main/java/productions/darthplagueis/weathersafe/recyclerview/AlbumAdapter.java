package productions.darthplagueis.weathersafe.recyclerview;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;

import static productions.darthplagueis.weathersafe.util.ScreenDimensions.getOneHalfDimens;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private final LayoutInflater inflater;
    private Fragment fragment;

    private List<UserAlbum> userAlbumList;

    private int imageDimensions;

    private UserMediaViewModel mediaViewModel;


    public AlbumAdapter(Context context, Fragment fragment) {
        inflater = LayoutInflater.from(context);
        imageDimensions = getOneHalfDimens(context);
        mediaViewModel = ViewModelProviders.of(fragment).get(UserMediaViewModel.class);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.useralbum_item, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.onBind(userAlbumList.get(position));
    }

    @Override
    public int getItemCount() {
        return userAlbumList != null ? userAlbumList.size() : 0;
    }

    public void setUserAlbumList(List<UserAlbum> userAlbumData) {
        userAlbumList = userAlbumData;
        notifyDataSetChanged();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        AlbumViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album_imageView);
            textView = itemView.findViewById(R.id.album_text);
        }

        void onBind(UserAlbum item) {
            mediaViewModel.getSingleEntityFromDirectory(item.getDirectoryName()).observe(fragment, userMedia -> {
                if (userMedia != null && userMedia.size() != 0) {
                    Log.d("AlbumAdapter", "onBind: " + userMedia.size());
                    Glide.with(itemView)
                            .load(new File(userMedia.get(0).getMediaFilePath()))
                            .apply(new RequestOptions().override(imageDimensions, imageDimensions))
                            .into(imageView);
                }
            });
            textView.setText(item.getDirectoryName());
        }
    }
}
