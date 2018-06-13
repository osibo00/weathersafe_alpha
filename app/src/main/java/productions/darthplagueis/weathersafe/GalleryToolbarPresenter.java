package productions.darthplagueis.weathersafe;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.util.FileManager;

import static productions.darthplagueis.weathersafe.util.Constants.SORT_ALBUMS;
import static productions.darthplagueis.weathersafe.util.Constants.SORT_ASCENDING;
import static productions.darthplagueis.weathersafe.util.Constants.SORT_DESCENDING;
import static productions.darthplagueis.weathersafe.util.CurrentDate.getDateString;
import static productions.darthplagueis.weathersafe.util.CurrentDate.getTimeStamp;

public class GalleryToolbarPresenter implements GalleryToolbarContract.presenter {

    private List<UserMedia> itemList = new ArrayList<>();

    private GalleryToolbarContract.GalleryView galleryView;
    private GalleryToolbarContract.ToolbarView toolbarView;

    private boolean isDirectorySaved;

    private static GalleryToolbarPresenter INSTANCE;

    public static GalleryToolbarPresenter getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GalleryToolbarPresenter();
        }
        return INSTANCE;
    }

    @Override
    public void setGalleryView(GalleryToolbarContract.GalleryView view) {
        this.galleryView = view;
    }

    @Override
    public void setToolbarView(GalleryToolbarContract.ToolbarView view) {
        this.toolbarView = view;
    }

    @Override
    public void onSelectionStateChange(boolean isMultiSelection) {
        if (!isMultiSelection) {
            itemList.clear();
            toolbarView.enableActionState();
            galleryView.enableActionState();
        } else {
            disableActionState();
        }
    }

    @Override
    public void itemSelectionMade(int amountSelected, UserMedia item) {
        if (itemList.contains(item)) {
            itemList.remove(item);
        } else {
            itemList.add(item);
        }
        toolbarView.notifyAmountSelected(String.valueOf(amountSelected));
    }

    @Override
    public void exitActionView() {
        galleryView.disableActionState();
    }

    @Override
    public void newAlbumPrompt() {
        if (itemList.size() == 0) return;
        galleryView.newAlbumDialog();
    }

    @Override
    public void deletePrompt() {
        if (itemList.size() == 0) return;
        galleryView.deleteDialog();
    }

    @Override
    public void createMediaFile(File file) {
        createMediaEntity(file);
    }

    @Override
    public void createNewAlbum(FileManager fileManager, UserAlbum userAlbum) {
        fileManager.setNewDirectoryName(userAlbum.getDirectoryName());
        moveSelectedItems(fileManager, userAlbum.getDirectoryTag());
    }

    @Override
    public void delete(FileManager fileManager) {
        deleteSelectedItems(fileManager);
    }

    @Override
    public void sortGallery(String action) {
        switch (action) {
            case SORT_ASCENDING:
                galleryView.sortByAscending();
                break;
            case SORT_DESCENDING:
                galleryView.sortByDescending();
                break;
            case SORT_ALBUMS:
                galleryView.sortByAlbum();
                break;
            default:
                break;
        }
    }

    private void disableActionState() {
        toolbarView.disableActionState();
        galleryView.disableActionState();
    }

    private void createMediaEntity(File file) {
        UserMedia userMedia = new UserMedia(file.getName(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4), getDateString(), getTimeStamp());
        galleryView.addToGallery(userMedia);
    }

    private void createMediaEntityWithTag(File file, String tags) {
        UserMedia userMedia = new UserMedia(file.getName(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4), tags, getDateString(), getTimeStamp());
        galleryView.addToGallery(userMedia);
    }

    private void moveSelectedItems(FileManager fileManager, String tags) {
        for (UserMedia item : itemList) {
            fileManager.setCurrentDirectoryName(item.getMediaFileDirectory());
            createMediaEntityWithTag(fileManager.moveFile(item.getMediaFileName()), tags);
            galleryView.removeFromGallery(item);
        }
        disableActionState();
    }

    private void deleteSelectedItems(FileManager fileManager) {
        for (UserMedia item : itemList) {
            fileManager.setCurrentDirectoryName(item.getMediaFileDirectory());
            fileManager.deleteFile(item.getMediaFileName());
            galleryView.removeFromGallery(item);
        }
        disableActionState();
    }
}
