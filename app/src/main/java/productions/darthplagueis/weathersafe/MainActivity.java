package productions.darthplagueis.weathersafe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import productions.darthplagueis.weathersafe.util.FileManager;
import productions.darthplagueis.weathersafe.viewmodel.UserMediaViewModel;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_CODE = 1987;
    private UserMediaViewModel mediaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_folder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new SecretGalleryFragment())
                .commit();

        mediaViewModel = ViewModelProviders.of(this).get(UserMediaViewModel.class);

        Log.d("MainActivity", "saveToDatabase: " + getDateString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == PICK_IMAGE_CODE && data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                FileManager fileManager = new FileManager(this);
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        //saveToDatabase(fileManager.saveBitmap(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveToDatabase(String filePath) {

        //UserMedia newItem = new UserMedia("", filePath, "", 11, 11, 1990);
        //mediaViewModel.insert(newItem);
    }

    private String getDateString() {
        long currentTimeMillis = System.currentTimeMillis();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(currentTimeMillis);
        return dateFormat.format(date);
    }

//    @Override
//    public void actionSelectionEnabled() {
//        if (getSupportActionBar() != null) {
//            if (getSupportActionBar().isShowing()) {
//                getSupportActionBar().hide();
//                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
//                Log.d("MainActivity", "actionSelectionEnabled: ");
//            } else {
//                getSupportActionBar().show();
//                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
//            }
//        }
//    }
//
//    @Override
//    public void actionPickItems() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(Intent.createChooser(intent, "action_pick_images"), PICK_IMAGE_CODE);
//    }
//
//    @Override
//    public void actionDeleteItems(List<UserMedia> userMedia) {
//
//    }
//
//    @Override
//    public void actionCopyItems(List<UserMedia> userMedia) {
//
//    }
//
//    @Override
//    public void actionCreateFolder() {
//
//    }
}
