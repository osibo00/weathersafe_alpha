package productions.darthplagueis.weathersafe;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryClickListener;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;
import productions.darthplagueis.weathersafe.util.FileManager;

import static productions.darthplagueis.weathersafe.util.Constants.ACTION_PICK_PHOTOS;
import static productions.darthplagueis.weathersafe.util.Constants.DELETE;
import static productions.darthplagueis.weathersafe.util.Constants.MOVE;
import static productions.darthplagueis.weathersafe.util.Constants.NEW_ALBUM;
import static productions.darthplagueis.weathersafe.util.Constants.PICK_IMAGE_CODE;
import static productions.darthplagueis.weathersafe.util.CurrentDate.getDateString;
import static productions.darthplagueis.weathersafe.util.CurrentDate.getTimeStamp;

public class GalleryPresenter implements GalleryContract.Presenter {

    private Set<UserMedia> itemSet = new HashSet<>();
    private Context context;
    private GalleryContract.View viewImpl;
    private GalleryMediaAdapter adapter;

    public GalleryPresenter(Context context, GalleryContract.View viewImpl) {
        this.context = context;
        this.viewImpl = viewImpl;
    }

    @Override
    public void setController() {
        onCreateAdapter();
    }

    @Override
    public void disablePhotoMultiSelect() {
        adapter.disableMultiSelection();
    }

    @Override
    public void retrieveMedia(Fragment fragment) {
        createPhotoPickerIntent(fragment);
    }

    @Override
    public void photoPickerIntentResult(int requestCode, Intent data) {
        createBitmapFromIntent(requestCode, data);
    }

    @Override
    public void getSelectedItems(String albumName, String action) {
        switch (action) {
            case DELETE:
                deleteSelectedItems();
                break;
            case MOVE:
                moveSelectedItems(albumName);
                break;
            case NEW_ALBUM:
                moveSelectedItems(albumName);
                break;
            default:
                break;
        }
    }

    private void onCreateAdapter() {
        GalleryClickListener listener = new GalleryClickListener() {
            @Override
            public void onClick(UserMedia userMedia) {

            }

            @Override
            public void onMultiSelectClick(int amountSelected, UserMedia item) {
                if (itemSet.contains(item)) {
                    itemSet.remove(item);
                } else {
                    itemSet.add(item);
                }
                setAmountItemsSelected(amountSelected);
            }

            @Override
            public void onLongClick(boolean isMultiSelection) {
                itemSet.clear();
                viewImpl.onSelectionChange(isMultiSelection);
            }
        };
        adapter = new GalleryMediaAdapter(context, listener);
        viewImpl.setAdapter(adapter);
    }

    private void setAmountItemsSelected(int amountSelected) {
        String amount = String.valueOf(amountSelected) +
                context.getResources().getString(R.string.items_selected);
        viewImpl.amountOfItemsSelected(amount);
    }

    private void createPhotoPickerIntent(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        fragment.startActivityForResult(Intent.createChooser(intent, ACTION_PICK_PHOTOS), PICK_IMAGE_CODE);
    }

    private void createBitmapFromIntent(int requestCode, Intent data) {
        if (data != null) {
            if (requestCode == PICK_IMAGE_CODE && data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                FileManager fileManager = new FileManager(context);
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(context).getContentResolver(), uri);
                        storeBitmapToDatabase(fileManager.saveBitmap(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void moveSelectedItems(String albumName) {
        if (itemSet.size() == 0) return;
        FileManager fileMover = new FileManager(context);
        fileMover.setNewDirectoryName(albumName);
        for (UserMedia item : itemSet) {
            fileMover.setCurrentDirectoryName(item.getMediaFileDirectory());
            storeBitmapToDatabase(fileMover.moveFile(item.getMediaFileName()));
            viewImpl.deleteSelected(item);
        }
    }

    private void storeBitmapToDatabase(File file) {
        viewImpl.storeUserMedia(new UserMedia(file.getName(), file.getAbsolutePath(),
                file.getParentFile().getName().substring(4), "", getDateString(), getTimeStamp()));
    }

    private void deleteSelectedItems() {
        if (itemSet.size() == 0) return;
        for (UserMedia item : itemSet) {
            viewImpl.deleteSelected(item);
        }
    }
}
