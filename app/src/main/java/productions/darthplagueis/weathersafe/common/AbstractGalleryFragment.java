package productions.darthplagueis.weathersafe.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import productions.darthplagueis.weathersafe.GalleryContract;
import productions.darthplagueis.weathersafe.GalleryPresenter;
import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.recyclerview.GalleryItemOffsets;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;
import productions.darthplagueis.weathersafe.view.dialogs.DeleteDialog;

import static productions.darthplagueis.weathersafe.util.Constants.DELETE;
import static productions.darthplagueis.weathersafe.util.Constants.MOVE;
import static productions.darthplagueis.weathersafe.util.Constants.NEW_ALBUM;


public abstract class AbstractGalleryFragment extends Fragment implements GalleryContract.View {

    public GalleryContract.Presenter presenter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView utilBarText;

    protected abstract int getLayoutId();

    protected abstract void onCreateView(View v);

    protected abstract void onAttachViewModel(GalleryMediaAdapter adapter);

    protected abstract int getDefaultToolbarLayoutId();

    protected abstract int getGridSpanCount();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(getLayoutId(), container, false);
        setToolbar(parentView);
        setRecyclerView(parentView);
        onCreateView(parentView);
        presenter = new GalleryPresenter(getContext(), this);
        presenter.setController();
        return parentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.photoPickerIntentResult(requestCode, data);
    }

    @Override
    public void setAdapter(GalleryMediaAdapter adapter) {
        recyclerView.setAdapter(adapter);
        onAttachViewModel(adapter);
    }


    @Override
    public void onSelectionChange(boolean isMultiSelection) {
        if (isMultiSelection) {
            switchToolbar(R.layout.gallery_toolbar_action);
        } else {
            switchToolbar(getDefaultToolbarLayoutId());
        }
    }

    @Override
    public void amountOfItemsSelected(String amount) {
        if (utilBarText != null) utilBarText.setText(amount);
    }

    public void deleteSelectedItems() {
        presenter.getSelectedItems("", DELETE);
        getDefaultToolbarChildView();
    }

    public void moveSelectedItems() {
        presenter.getSelectedItems("", MOVE);
        getDefaultToolbarChildView();
    }

    public void createNewAlbum(String albumName) {
        presenter.getSelectedItems(albumName, NEW_ALBUM);
        getDefaultToolbarChildView();
    }

    private void setToolbar(View parentView) {
        toolbar = parentView.findViewById(R.id.gallery_toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        getLayoutInflater().inflate(getDefaultToolbarLayoutId(), toolbar);
    }

    private void setRecyclerView(View parentView) {
        recyclerView = parentView.findViewById(R.id.gallery_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getGridSpanCount()));
        recyclerView.addItemDecoration(new GalleryItemOffsets(parentView.getContext()));
    }

    private void switchToolbar(int layout) {
        toolbar.removeAllViews();
        View v = getLayoutInflater().inflate(layout, toolbar);

        switch (layout) {
            case R.layout.gallery_toolbar_action:
                setStatusBarColor(R.color.colorAccent);
                utilBarText = v.findViewById(R.id.utilBar_text);
                v.findViewById(R.id.close_btn).setOnClickListener(v1 -> getDefaultToolbarChildView());
                v.findViewById(R.id.delete_btn).setOnClickListener(v12 -> deleteItemsPrompt());
                v.findViewById(R.id.copy_btn).setOnClickListener(v13 -> moveSelectedItems());
                v.findViewById(R.id.create_folder_btn).setOnClickListener(v14 -> createAlbumPrompt());
                break;
            default:
                setStatusBarColor(R.color.colorPrimaryDark);
                break;
        }
    }

    private void setStatusBarColor(int color) {
        Objects.requireNonNull(getActivity()).getWindow()
                .setStatusBarColor(ContextCompat.getColor(getActivity(), color));
    }

    private void getDefaultToolbarChildView() {
        presenter.disablePhotoMultiSelect();
        switchToolbar(getDefaultToolbarLayoutId());
    }

    private void createAlbumPrompt() {
//        CreateAlbumDialog dialog = CreateAlbumDialog.newInstance(this);
//        dialog.show(getChildFragmentManager(), CREATE_ALBUM_DIALOG_TAG);
    }

    private void deleteItemsPrompt() {
        DeleteDialog dialog = DeleteDialog.newInstance();
        dialog.show(getChildFragmentManager(), "DELETE _DIALOG_TAG");
    }
}
