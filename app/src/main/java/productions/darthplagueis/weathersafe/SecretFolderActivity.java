package productions.darthplagueis.weathersafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import productions.darthplagueis.weathersafe.view.AlbumsFragment;
import productions.darthplagueis.weathersafe.view.PhotosFragment;

public class SecretFolderActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_folder);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        setListener(navigation);

        switchFragment(new PhotosFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void switchFragment(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void setListener(BottomNavigationView navigation) {
        BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_photos:
                    switchFragment(new PhotosFragment());
                    return true;
                case R.id.navigation_albums:
                    switchFragment(new AlbumsFragment());
                    return true;
                case R.id.navigation_favorites:

                    return true;
                case R.id.navigation_notes:

                    return true;
            }
            return false;
        };
        navigation.setOnNavigationItemSelectedListener(listener);
    }
}
