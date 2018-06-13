package productions.darthplagueis.weathersafe.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.recyclerview.AlbumAdapter;
import productions.darthplagueis.weathersafe.recyclerview.AlbumItemOffsets;
import productions.darthplagueis.weathersafe.viewmodel.UserAlbumViewModel;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;


public class AlbumsFragment extends Fragment {

    private List<String> albumNames;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
        AlbumAdapter adapter = new AlbumAdapter(getContext(), this);
        RecyclerView recyclerView = rootView.findViewById(R.id.album_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new AlbumItemOffsets(rootView.getContext()));

        UserAlbumViewModel viewModel = ViewModelProviders.of(this).get(UserAlbumViewModel.class);
        Observer<List<UserAlbum>> observer = userAlbums -> {
            if (userAlbums != null && userAlbums.size() != 0) adapter.setUserAlbumList(userAlbums);
        };
        viewModel.getDescDateList().observe(this, observer);
        return rootView;
    }

}
