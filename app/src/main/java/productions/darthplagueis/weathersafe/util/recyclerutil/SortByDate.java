package productions.darthplagueis.weathersafe.util.recyclerutil;

import java.util.Comparator;

import productions.darthplagueis.weathersafe.model.UserMedia;

public class SortByDate implements Comparator<UserMedia> {

    @Override
    public int compare(UserMedia o1, UserMedia o2) {
        return Long.compare(o1.getMediaTimeStamp(), o2.getMediaTimeStamp());
    }
}
