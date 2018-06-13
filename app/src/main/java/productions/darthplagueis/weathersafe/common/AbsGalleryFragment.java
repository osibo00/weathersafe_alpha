package productions.darthplagueis.weathersafe.common;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Objects;

import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.recyclerview.GalleryItemOffsets;
import productions.darthplagueis.weathersafe.util.FileManager;
import productions.darthplagueis.weathersafe.view.dialogs.CreateAlbumDialog;
import productions.darthplagueis.weathersafe.view.dialogs.DeleteDialog;

import static productions.darthplagueis.weathersafe.util.Constants.ACTION_PICK_PHOTOS;
import static productions.darthplagueis.weathersafe.util.Constants.CREATE_ALBUM_DIALOG_TAG;
import static productions.darthplagueis.weathersafe.util.Constants.DELETE_DIALOG_TAG;
import static productions.darthplagueis.weathersafe.util.Constants.PICK_IMAGE_CODE;


public abstract class AbsGalleryFragment extends Fragment {

    protected abstract int getLayoutId();

    protected abstract void createView(View v);

    protected abstract int getToolbarLayoutId();

    protected abstract int getGridSpanCount();

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract void createUserMedia(File file);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(getLayoutId(), container, false);

        createView(parentView);

        setToolbar(inflater, parentView);

        setRecyclerView(parentView);

        return parentView;
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
                        createUserMedia(fileManager.saveBitmap(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void importPhotos() {
        Intent importPhotosIntent = new Intent(Intent.ACTION_PICK);
        importPhotosIntent.setType("image/*");
        importPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(importPhotosIntent, ACTION_PICK_PHOTOS), PICK_IMAGE_CODE);
    }

    protected void setStatusBarColor(int color) {
        Objects.requireNonNull(getActivity()).getWindow()
                .setStatusBarColor(ContextCompat.getColor(getActivity(), color));
    }

    protected void createDeleteDialog() {
        DeleteDialog dialog = DeleteDialog.newInstance();
        dialog.show(getChildFragmentManager(), DELETE_DIALOG_TAG);
    }

    private void setToolbar(@NonNull LayoutInflater inflater, View parentView) {
        Toolbar toolbar = parentView.findViewById(R.id.gallery_toolbar);
        inflater.inflate(getToolbarLayoutId(), toolbar);
    }

    private void setRecyclerView(View parentView) {
        RecyclerView recyclerView = parentView.findViewById(R.id.gallery_recyclerView);
        recyclerView.setAdapter(getAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getGridSpanCount()));
        recyclerView.addItemDecoration(new GalleryItemOffsets(parentView.getContext()));
    }
}
