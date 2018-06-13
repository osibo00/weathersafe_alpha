package productions.darthplagueis.weathersafe;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Set;

import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;

public interface GalleryContract {

    interface View {
        void setAdapter(GalleryMediaAdapter adapter);

        void onSelectionChange(boolean isMultiSelection);

        void amountOfItemsSelected(String amount);

        void storeUserMedia(UserMedia item);

        void deleteSelected(UserMedia item);
    }

    interface Presenter {
        void setController();

        void disablePhotoMultiSelect();

        void retrieveMedia(Fragment fragment);

        void photoPickerIntentResult(int requestCode, Intent data);

        void getSelectedItems(String albumName, String action);
    }
}
