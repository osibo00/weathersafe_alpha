package productions.darthplagueis.weathersafe.view.layouts;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import productions.darthplagueis.weathersafe.GalleryToolbarContract;
import productions.darthplagueis.weathersafe.GalleryToolbarPresenter;
import productions.darthplagueis.weathersafe.R;

import static productions.darthplagueis.weathersafe.util.Constants.SORT_ALBUMS;
import static productions.darthplagueis.weathersafe.util.Constants.SORT_ASCENDING;
import static productions.darthplagueis.weathersafe.util.Constants.SORT_DESCENDING;

public class ActionToolbarLayout extends ConstraintLayout implements
        GalleryToolbarContract.ToolbarView,
        View.OnClickListener {

    private List<View> defaultView = new ArrayList<>();
    private List<View> actionView = new ArrayList<>();

    private AppCompatImageButton searchBtn, sortBtn, filterBtn, settingsBtn,
            exitActionViewBtn, newAlbumBtn, copyBtn, deleteBtn;
    private TextView utilBarText;

    private String itemsSelected;

    private GalleryToolbarContract.presenter presenter;

    public ActionToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setDefaultView();
        setActionView();

        itemsSelected = getContext().getResources().getString(R.string.items_selected);

        presenter = GalleryToolbarPresenter.getINSTANCE();
        presenter.setToolbarView(this);
    }

    @Override
    public void enableActionState() {
        utilBarText.setText("");
        setDefaultVisibility(GONE);
        setActionVisibility(VISIBLE);
        setToolbarColor(R.color.colorAccent);
    }

    @Override
    public void disableActionState() {
        setActionVisibility(GONE);
        setDefaultVisibility(VISIBLE);
        setToolbarColor(R.color.colorPrimary);
    }

    @Override
    public void notifyAmountSelected(String amountSelected) {
        utilBarText.setText(String.format("%s%s", amountSelected, itemsSelected));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_btn:
                onSort();
                break;
            case R.id.filter_btn:
                break;
            case R.id.close_btn:
                onClose();
                break;
            case R.id.create_folder_btn:
                onCreateAlbum();
                break;
            case R.id.delete_btn:
                onConfirmDelete();
                break;
            default:
                break;
        }
    }

    private void setDefaultView() {
        searchBtn = findViewById(R.id.search_btn);
        defaultView.add(searchBtn);
        sortBtn = findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(this);
        defaultView.add(sortBtn);
        filterBtn = findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(this);
        defaultView.add(filterBtn);
        settingsBtn = findViewById(R.id.settings_btn);
        defaultView.add(settingsBtn);
    }

    private void setActionView() {
        exitActionViewBtn = findViewById(R.id.close_btn);
        exitActionViewBtn.setOnClickListener(this);
        actionView.add(exitActionViewBtn);
        newAlbumBtn = findViewById(R.id.create_folder_btn);
        newAlbumBtn.setOnClickListener(this);
        actionView.add(newAlbumBtn);
        copyBtn = findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(this);
        actionView.add(copyBtn);
        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(this);
        actionView.add(deleteBtn);
        utilBarText = findViewById(R.id.utilBar_text);
        actionView.add(utilBarText);
    }

    private void onSort() {
        PopupMenu popupMenu = new PopupMenu(getContext(), sortBtn, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.sorting_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.ascending_order:
                    presenter.sortGallery(SORT_ASCENDING);
                    return true;
                case R.id.descending_order:
                    presenter.sortGallery(SORT_DESCENDING);
                    return true;
                case R.id.album_order:
                    presenter.sortGallery(SORT_ALBUMS);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void onClose() {
        setActionVisibility(GONE);
        setDefaultVisibility(VISIBLE);
        setToolbarColor(R.color.colorPrimary);
        presenter.exitActionView();
    }

    private void onConfirmDelete() {
        presenter.deletePrompt();
    }

    private void onCreateAlbum() {
        presenter.newAlbumPrompt();
    }

    private void setToolbarColor(int color) {
        setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    private void setDefaultVisibility(int visibility) {
        for (View v : defaultView) {
            v.setVisibility(visibility);
        }
    }

    private void setActionVisibility(int visibility) {
        for (View v : actionView) {
            v.setVisibility(visibility);
        }
    }
}
