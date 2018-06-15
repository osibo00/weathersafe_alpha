package productions.darthplagueis.weathersafe.common;

import android.animation.Animator;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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


public abstract class AbsGalleryFragment extends Fragment implements View.OnClickListener {

    private View fabBackground;
    private LinearLayout fabLayout01, fabLayout02, fabLayout03;
    private FloatingActionButton fab;

    private boolean isFabActive;

    protected abstract int getLayoutId();

    protected abstract void createView(View v);

    protected abstract int getToolbarLayoutId();

    protected abstract int getGridSpanCount();

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract void createUserMedia(File file);

    protected abstract void selectPhotos();

    protected abstract void movePhotos();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(getLayoutId(), container, false);

        createView(parentView);

        setToolbar(inflater, parentView);

        setRecyclerView(parentView);

        setFloatingActionButton(parentView);

        return parentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photos_fab:
                if (!isFabActive) showFabMenu();
                else closeFabMenu();
                break;
            case R.id.fab_background:
                closeFabMenu();
                break;
            case R.id.mini_fab01:
                importPhotos();
                closeFabMenu();
                break;
            case R.id.mini_fab02:
                movePhotos();
                break;
            case R.id.mini_fab03:
                selectPhotos();
                break;
            default:
                break;
        }
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

    private void setFloatingActionButton(View parentView) {
        fabBackground = parentView.findViewById(R.id.fab_background);
        fabBackground.setOnClickListener(this);
        fabLayout01 = parentView.findViewById(R.id.fab_layout01);
        fabLayout02 = parentView.findViewById(R.id.fab_layout02);
        fabLayout03 = parentView.findViewById(R.id.fab_layout03);
        fab = parentView.findViewById(R.id.photos_fab);
        fab.setOnClickListener(this);
        parentView.findViewById(R.id.mini_fab01).setOnClickListener(this);
        parentView.findViewById(R.id.mini_fab02).setOnClickListener(this);
        parentView.findViewById(R.id.mini_fab03).setOnClickListener(this);
    }

    private void showFabMenu() {
        isFabActive = true;
        fabLayout01.setVisibility(View.VISIBLE);
        fabLayout02.setVisibility(View.VISIBLE);
        fabLayout03.setVisibility(View.VISIBLE);
        fabBackground.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(135f);
        fabLayout01.animate().translationY(-getResources().getDimension(R.dimen.dp_55));
        fabLayout02.animate().translationY(-getResources().getDimension(R.dimen.dp_100));
        fabLayout03.animate().translationY(-getResources().getDimension(R.dimen.dp_145));
    }

    private void closeFabMenu() {
        isFabActive = false;
        fabBackground.setVisibility(View.GONE);
        fab.animate().rotationBy(-135f);
        fabLayout01.animate().translationY(0);
        fabLayout02.animate().translationY(0);
        fabLayout03.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFabActive) {
                    fabLayout01.setVisibility(View.GONE);
                    fabLayout02.setVisibility(View.GONE);
                    fabLayout03.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void importPhotos() {
        Intent importPhotosIntent = new Intent(Intent.ACTION_PICK);
        importPhotosIntent.setType("image/*");
        importPhotosIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(importPhotosIntent, ACTION_PICK_PHOTOS), PICK_IMAGE_CODE);
    }
}
