package productions.darthplagueis.weathersafe;


import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryClickListener;
import productions.darthplagueis.weathersafe.recyclerview.GalleryItemOffsets;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;
import productions.darthplagueis.weathersafe.util.FileManager;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;

import static productions.darthplagueis.weathersafe.util.Constants.ACTION_PICK_PHOTOS;
import static productions.darthplagueis.weathersafe.util.Constants.PICK_IMAGE_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements GalleryToolbarContract.GalleryView {

    private GalleryMediaAdapter mediaAdapter;
    private UserMediaViewModel viewModel;

    private GalleryToolbarContract.presenter presenter;

    public BlankFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        presenter = GalleryToolbarPresenter.getINSTANCE();
        presenter.setGalleryView(this);

        Toolbar toolbar = rootView.findViewById(R.id.gallery_toolbar);
        getLayoutInflater().inflate(R.layout.gallery_toolbar_action, toolbar);

        GalleryClickListener listener = getGalleryClickListener();
        mediaAdapter = new GalleryMediaAdapter(getContext(), listener);

        RecyclerView recyclerView = rootView.findViewById(R.id.gallery_recyclerView);
        recyclerView.setAdapter(mediaAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new GalleryItemOffsets(rootView.getContext()));

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> importPhotos());

        viewModel = ViewModelProviders.of(BlankFragment.this).get(UserMediaViewModel.class);
        viewModel.getAllMedia().observe(BlankFragment.this, mediaAdapter::setUserMediaList);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == PICK_IMAGE_CODE && data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                FileManager fileManager = new FileManager(getContext());
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(
                                getContext()).getContentResolver(), uri);
                        presenter.createMediaFile(fileManager.saveBitmap(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void addToGallery(UserMedia userMedia) {
        viewModel.insert(userMedia);
    }

    @Override
    public void enableActionState() {
        setStatusBarColor(R.color.colorAccent);
        mediaAdapter.enableMultiSelection();
    }

    @Override
    public void disableActionState() {
        setStatusBarColor(R.color.colorPrimaryDark);
        mediaAdapter.disableMultiSelection();
    }

    @Override
    public void newAlbumDialog() {

    }

    @Override
    public void removeFromGallery(UserMedia userMedia) {

    }

    @Override
    public void deleteDialog() {

    }

    @Override
    public void sortByAscending() {

    }

    @Override
    public void sortByDescending() {

    }

    @Override
    public void sortByAlbum() {

    }

    @Override
    public void addToAlbums(UserAlbum userAlbum) {

    }

    @NonNull
    private GalleryClickListener getGalleryClickListener() {
        return new GalleryClickListener() {
            @Override
            public void onClick(UserMedia userMedia) {

            }

            @Override
            public void onMultiSelectClick(int amountSelected, UserMedia item) {

            }

            @Override
            public void onLongClick(boolean isMultiSelection) {
                presenter.onSelectionStateChange(isMultiSelection);
            }
        };
    }

    private void setStatusBarColor(int color) {
        Objects.requireNonNull(getActivity()).getWindow()
                .setStatusBarColor(ContextCompat.getColor(getActivity(), color));
    }

    private void importPhotos() {
        Intent importPhotosIntent = new Intent(Intent.ACTION_PICK);
        importPhotosIntent.setType("image/*");
        importPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(importPhotosIntent, ACTION_PICK_PHOTOS), PICK_IMAGE_CODE);
    }

}
