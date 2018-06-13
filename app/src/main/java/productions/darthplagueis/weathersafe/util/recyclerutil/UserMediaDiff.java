package productions.darthplagueis.weathersafe.util.recyclerutil;

import android.support.v7.util.DiffUtil;

import java.util.List;

import productions.darthplagueis.weathersafe.model.UserMedia;

public class UserMediaDiff extends DiffUtil.Callback {

    private List<UserMedia> oldList;
    private List<UserMedia> newList;

    public UserMediaDiff (List<UserMedia> oldList, List<UserMedia> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getMediaFileName().equals(newList.get(newItemPosition).getMediaFileName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getMediaFilePath().equals(newList.get(newItemPosition).getMediaFilePath());
    }
}
