package productions.darthplagueis.weathersafe.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.List;

import productions.darthplagueis.weathersafe.GalleryToolbarContract;
import productions.darthplagueis.weathersafe.GalleryToolbarPresenter;
import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.SecretFolderActivity;
import productions.darthplagueis.weathersafe.common.AbsGalleryFragment;
import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryClickListener;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;

import productions.darthplagueis.weathersafe.view.dialogs.CreateAlbumDialog;
import productions.darthplagueis.weathersafe.viewmodel.UserAlbumViewModel;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;

import static productions.darthplagueis.weathersafe.util.Constants.CREATE_ALBUM_DIALOG_TAG;


public class PhotosFragment extends AbsGalleryFragment implements GalleryToolbarContract.GalleryView {

    private GalleryMediaAdapter adapter;

    private UserMediaViewModel mediaViewModel;
    private UserAlbumViewModel albumViewModel;
    private Observer<List<UserMedia>> observer;

    private GalleryToolbarContract.presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fab;
    }

    @Override
    protected void createView(View v) {
        presenter = GalleryToolbarPresenter.getINSTANCE();
        setAdapter();
        presenter.setGalleryView(this);

        mediaViewModel = ViewModelProviders.of(this).get(UserMediaViewModel.class);
        albumViewModel = ViewModelProviders.of(this).get(UserAlbumViewModel.class);

        observer = adapter::setUserMediaList;
        mediaViewModel.getDescDateList().observe(this, observer);

//        FloatingActionButton fab = v.findViewById(R.id.fab);
//        fab.setOnClickListener(v1 -> importPhotos());
    }

    @Override
    protected int getToolbarLayoutId() {
        return R.layout.gallery_toolbar_action;
    }

    @Override
    protected int getGridSpanCount() {
        return getResources().getInteger(R.integer.gallery_grid_span);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void createUserMedia(File file) {
        presenter.createMediaFile(file);
    }

    @Override
    public void addToGallery(UserMedia userMedia) {
        mediaViewModel.insert(userMedia);
    }

    @Override
    public void removeFromGallery(UserMedia userMedia) {
        mediaViewModel.delete(userMedia);
    }

    @Override
    public void enableActionState() {
        setStatusBarColor(R.color.colorAccent);
        adapter.enableMultiSelection();
    }

    @Override
    public void disableActionState() {
        setStatusBarColor(R.color.colorPrimaryDark);
        adapter.disableMultiSelection();
    }

    @Override
    public void newAlbumDialog() {
        createAlbumDialog();
    }

    @Override
    public void deleteDialog() {
        createDeleteDialog();
    }

    @Override
    public void sortByAscending() {
        checkForObservers();
        mediaViewModel.getAscDateList().observe(this, observer);
    }

    @Override
    public void sortByDescending() {
        checkForObservers();
        mediaViewModel.getDescDateList().observe(this, observer);
    }

    @Override
    public void sortByAlbum() {
        checkForObservers();
        mediaViewModel.getAlbumOrderList().observe(this, observer);
    }

    @Override
    public void addToAlbums(UserAlbum userAlbum) {
        //albumViewModel.insert(userAlbum);
    }

    private void setAdapter() {
        GalleryClickListener listener = getGalleryClickListener();
        adapter = new GalleryMediaAdapter(getContext(), listener);
    }

    private void checkForObservers() {
        if (mediaViewModel.getDescDateList().hasObservers()) {
            mediaViewModel.getDescDateList().removeObserver(observer);
        }
        if (mediaViewModel.getAscDateList().hasObservers()) {
            mediaViewModel.getAscDateList().removeObserver(observer);
        }
        if (mediaViewModel.getAlbumOrderList().hasObservers()) {
            mediaViewModel.getAlbumOrderList().removeObserver(observer);
        }
    }

    @NonNull
    private GalleryClickListener getGalleryClickListener() {
        return new GalleryClickListener() {
            @Override
            public void onClick(UserMedia userMedia) {
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setPhotoFilePath(userMedia.getMediaFilePath());
                ((SecretFolderActivity) getActivity()).addFragment(detailFragment);
            }

            @Override
            public void onMultiSelectClick(int amountSelected, UserMedia item) {
                presenter.itemSelectionMade(amountSelected, item);
            }

            @Override
            public void onLongClick(boolean isMultiSelection) {
                presenter.onSelectionStateChange(isMultiSelection);
            }
        };
    }

    private void createAlbumDialog() {
        CreateAlbumDialog dialog = CreateAlbumDialog.newInstance(presenter);
        dialog.show(getChildFragmentManager(), CREATE_ALBUM_DIALOG_TAG);
    }
}
