package productions.darthplagueis.weathersafe.recyclerview;

import productions.darthplagueis.weathersafe.model.UserMedia;

public interface GalleryClickListener {

    void onClick();

    void onMultiSelectClick(int amountSelected, UserMedia item);

    void onLongClick(boolean isMultiSelection);
}
