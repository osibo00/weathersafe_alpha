package productions.darthplagueis.weathersafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import productions.darthplagueis.weathersafe.view.AlbumsFragment;
import productions.darthplagueis.weathersafe.view.PhotosFragment;

public class SecretFolderActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView navigation;

    private boolean isNavigationHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_folder);
        navigation = findViewById(R.id.bottom_navigation);
        setNavigationListener();

        switchFragment(new PhotosFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            setNavigationVisibility();
        } else {
            super.onBackPressed();
        }
    }

    public void addFragment(Fragment fragment) {
        setNavigationVisibility();
        fragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void setNavigationVisibility() {
        if (!isNavigationHidden) {
            isNavigationHidden = true;
            navigation.setVisibility(View.GONE);
        } else {
            isNavigationHidden = false;
            navigation.setVisibility(View.VISIBLE);
        }
    }

    private void switchFragment(Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void setNavigationListener() {
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
