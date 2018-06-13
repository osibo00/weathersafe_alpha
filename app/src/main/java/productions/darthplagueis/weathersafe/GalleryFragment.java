package productions.darthplagueis.weathersafe;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import productions.darthplagueis.weathersafe.controller.UserMediaAdapter;
import productions.darthplagueis.weathersafe.model.UserMedia;
import productions.darthplagueis.weathersafe.recyclerview.GalleryItemOffsets;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;


public class GalleryFragment extends Fragment implements View.OnClickListener {

    private Toolbar buttonBar;
    private TextView buttonBarText;
    private UserMediaAdapter mediaAdapter;
    private GalleryActionCallBack statusCallBack;

    public GalleryFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GalleryActionCallBack) {
            statusCallBack = (GalleryActionCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GalleryActionCallBack");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView recyclerView = setViews(rootView);

        UserMediaAdapter.OnItemsSelectedListener listener = new UserMediaAdapter.OnItemsSelectedListener() {
            @Override
            public void multiSelectionEnabled(boolean enabled) {
                if (enabled) {
                    statusCallBack.actionSelectionEnabled();
                    buttonBar.setVisibility(View.VISIBLE);
                } else {
                    statusCallBack.actionSelectionEnabled();
                    buttonBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void amountSelected(int itemCount) {
                String text = String.valueOf(itemCount) + getResources().getString(R.string.items_selected);
                buttonBarText.setText(text);
            }
        };
        mediaAdapter = new UserMediaAdapter(getContext(), listener);
        recyclerView.setAdapter(mediaAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new GalleryItemOffsets(rootView.getContext()));

        UserMediaViewModel viewModel = ViewModelProviders.of(GalleryFragment.this).get(UserMediaViewModel.class);
        viewModel.getAllMedia().observe(GalleryFragment.this, new Observer<List<UserMedia>>() {
            @Override
            public void onChanged(@Nullable List<UserMedia> userMedia) {
                mediaAdapter.setUserMediaList(userMedia);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        statusCallBack = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                statusCallBack.actionPickItems();
            case R.id.close_btn:
                mediaAdapter.quitMultiSelection();
                Log.d("Gallery", "quitMultiSelection: quit");
                break;
            case R.id.delete_btn:
                statusCallBack.actionDeleteItems(mediaAdapter.getUserMedia());
                break;
            case R.id.copy_btn:
                statusCallBack.actionCopyItems(mediaAdapter.getUserMedia());
                break;
            case R.id.create_folder_btn:
                statusCallBack.actionCreateFolder();
                break;
            default:
                break;
        }

    }

    private RecyclerView setViews(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.gallery_recyclerView);
        //buttonBar = rootView.findViewById(R.id.gallery_buttonBar);
        rootView.findViewById(R.id.fab).setOnClickListener(this);
        rootView.findViewById(R.id.close_btn).setOnClickListener(this);
        rootView.findViewById(R.id.delete_btn).setOnClickListener(this);
        rootView.findViewById(R.id.copy_btn).setOnClickListener(this);
        rootView.findViewById(R.id.create_folder_btn).setOnClickListener(this);
        buttonBarText = rootView.findViewById(R.id.utilBar_text);
        return recyclerView;
    }

    public interface GalleryActionCallBack {
        void actionSelectionEnabled();

        void actionPickItems();

        void actionDeleteItems(List<UserMedia> userMedia);

        void actionCopyItems(List<UserMedia> userMedia);

        void actionCreateFolder();
    }
}
