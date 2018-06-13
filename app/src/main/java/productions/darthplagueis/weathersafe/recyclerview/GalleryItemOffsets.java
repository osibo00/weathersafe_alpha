package productions.darthplagueis.weathersafe.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import productions.darthplagueis.weathersafe.R;

public class GalleryItemOffsets extends RecyclerView.ItemDecoration {

    private int insetMargin;
    private int noMargin;

    public GalleryItemOffsets(Context context) {
        insetMargin = context.getResources().getDimensionPixelSize(R.dimen.recycler_view_margins);
        noMargin = context.getResources().getDimensionPixelSize(R.dimen.no_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                outRect.top = insetMargin;
            default:
                outRect.bottom = insetMargin;
                break;
        }
        outRect.right = (position + 1) % 4 == 0 ? noMargin : insetMargin;
    }
}
