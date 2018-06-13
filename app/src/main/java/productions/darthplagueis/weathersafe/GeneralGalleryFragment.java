package productions.darthplagueis.weathersafe;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import productions.darthplagueis.weathersafe.common.AbstractGalleryFragment;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;


public class GeneralGalleryFragment extends AbstractGalleryFragment {

    private UserMediaViewModel viewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photos;
    }

    @Override
    protected int getDefaultToolbarLayoutId() {
        return R.layout.gallery_toolbar_normal;
    }

    @Override
    protected int getGridSpanCount() {
        return getResources().getInteger(R.integer.gallery_grid_span);
    }

    @Override
    protected void onCreateView(View v) {
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(v1 -> presenter.retrieveMedia(this));
    }

    @Override
    protected void onAttachViewModel(final GalleryMediaAdapter adapter) {
        viewModel = ViewModelProviders.of(GeneralGalleryFragment.this).get(UserMediaViewModel.class);
        viewModel.getAllMedia().observe(GeneralGalleryFragment.this, adapter::setUserMediaList);
    }

    @Override
    public void storeUserMedia(UserMedia item) {
        viewModel.insert(item);
    }

    @Override
    public void deleteSelected(UserMedia item) {
        viewModel.delete(item);
    }

}
