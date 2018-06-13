package productions.darthplagueis.weathersafe;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryMediaAdapter;
import productions.darthplagueis.weathersafe.recyclerview.GalleryItemOffsets;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;

import static productions.darthplagueis.weathersafe.util.Constants.DELETE;


public class SecretGalleryFragment extends Fragment implements GalleryContract.View {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView utilBarText;
    private int currentToolbarLayout;
    private GalleryContract.Presenter presenter;
    private UserMediaViewModel viewModel;

    public SecretGalleryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        setViews(rootView);
        presenter = new GalleryPresenter(getContext(), this);
        presenter.setController();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.photoPickerIntentResult(requestCode, data);
    }

    @Override
    public void setAdapter(GalleryMediaAdapter adapter) {
        recyclerView.setAdapter(adapter);
        populateRV(adapter);
    }

    @Override
    public void onSelectionChange(boolean isMultiSelection) {
        //switchToolbar(layout);
    }

    @Override
    public void amountOfItemsSelected(String amount) {
        if (utilBarText != null) {
            utilBarText.setText(amount);
        }
    }

    @Override
    public void storeUserMedia(UserMedia item) {
        viewModel.insert(item);
    }

    @Override
    public void deleteSelected(UserMedia item) {
        viewModel.delete(item);
    }

    private void setViews(View rootView) {
        toolbar = rootView.findViewById(R.id.gallery_toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        getLayoutInflater().inflate(R.layout.gallery_toolbar_normal, toolbar);
        recyclerView = rootView.findViewById(R.id.gallery_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(
                getContext(),
                getResources().getInteger(R.integer.gallery_grid_span)));
        recyclerView.addItemDecoration(new GalleryItemOffsets(rootView.getContext()));
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.retrieveMedia(SecretGalleryFragment.this);
            }
        });
    }

    private void populateRV(final GalleryMediaAdapter adapter) {
        viewModel = ViewModelProviders.of(SecretGalleryFragment.this).get(UserMediaViewModel.class);
        viewModel.getAllMedia().observe(SecretGalleryFragment.this, new Observer<List<UserMedia>>() {
            @Override
            public void onChanged(@Nullable List<UserMedia> userMedia) {
                adapter.setUserMediaList(userMedia);
            }
        });
    }

    private void switchToolbar(int layout) {
        if (currentToolbarLayout == layout) return;
        currentToolbarLayout = layout;
        toolbar.removeAllViews();
        View v = getLayoutInflater().inflate(layout, toolbar);

        switch (layout) {
            case R.layout.gallery_toolbar_action:
                setStatusBarColor(R.color.colorAccent);
                utilBarText = v.findViewById(R.id.utilBar_text);
                v.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.disablePhotoMultiSelect();
                        switchToolbar(R.layout.gallery_toolbar_normal);
                    }
                });
                v.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getSelectedItems("", DELETE);
                        presenter.disablePhotoMultiSelect();
                        switchToolbar(R.layout.gallery_toolbar_normal);
                    }
                });
                v.findViewById(R.id.create_folder_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case R.layout.gallery_toolbar_normal:
                setStatusBarColor(R.color.colorPrimaryDark);
                break;
            default:
                break;
        }
    }

    private void setStatusBarColor(int colorAccent) {
        Objects.requireNonNull(getActivity()).getWindow()
                .setStatusBarColor(ContextCompat
                        .getColor(getActivity(), colorAccent));
    }
}
