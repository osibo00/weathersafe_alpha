package productions.darthplagueis.weathersafe.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import productions.darthplagueis.weathersafe.R;

public class AlbumItemOffsets extends RecyclerView.ItemDecoration {

    private int insetMargin;
    private int halfInsetMargin;

    public AlbumItemOffsets(Context context) {
        insetMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_margin);
        halfInsetMargin = context.getResources().getDimensionPixelSize(R.dimen.childView_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        switch (position) {
            case 0:
            case 1:
                outRect.top = insetMargin;
            default:
                outRect.bottom = insetMargin;
                break;
        }
        outRect.left = (position % 2 != 0) ? halfInsetMargin : insetMargin;
        outRect.right = (position % 2 != 0) ? insetMargin : halfInsetMargin;
    }
}
