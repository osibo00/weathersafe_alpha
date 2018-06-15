package productions.darthplagueis.weathersafe.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

import productions.darthplagueis.weathersafe.R;

import static productions.darthplagueis.weathersafe.util.StatusBarColor.setStatusBarColor;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private String photoFilePath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setStatusBarColor(getActivity(), R.color.black38alpha);
        rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Toolbar toolbar = rootView.findViewById(R.id.detail_toolbar);
        ImageView imageView = rootView.findViewById(R.id.detail_imageView);
        imageView.setOnClickListener(v -> fullScreen(toolbar));

        Glide.with(rootView)
                .load(new File(photoFilePath))
                .into(imageView);

        return rootView;
    }

    private void fullScreen(Toolbar toolbar) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setStatusBarColor(getActivity(), R.color.colorPrimaryDark);
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

}
