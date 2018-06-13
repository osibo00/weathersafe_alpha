package productions.darthplagueis.weathersafe;

import java.io.File;

import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.util.FileManager;

public interface GalleryToolbarContract {

    interface GalleryView {
        void addToGallery(UserMedia userMedia);

        void removeFromGallery(UserMedia userMedia);

        void enableActionState();

        void disableActionState();

        void newAlbumDialog();

        void deleteDialog();

        void sortByAscending();

        void sortByDescending();

        void sortByAlbum();

        void addToAlbums(UserAlbum userAlbum);
    }

    interface ToolbarView {
        void enableActionState();

        void disableActionState();

        void notifyAmountSelected(String amountSelected);
    }

    interface presenter {
        void setGalleryView(GalleryView view);

        void setToolbarView(ToolbarView view);

        void onSelectionStateChange(boolean isMultiSelection);

        void itemSelectionMade(int amountSelected, UserMedia item);

        void exitActionView();

        void createMediaFile(File file);

        void createNewAlbum(FileManager fileManager, UserAlbum userAlbum);

        void delete(FileManager fileManager);

        void newAlbumPrompt();

        void deletePrompt();

        void sortGallery(String action);
    }
}
