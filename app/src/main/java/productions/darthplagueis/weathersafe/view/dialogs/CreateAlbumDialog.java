package productions.darthplagueis.weathersafe.view.dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import productions.darthplagueis.weathersafe.GalleryToolbarContract;
import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.model.UserAlbum;
import productions.darthplagueis.weathersafe.util.FileManager;
import productions.darthplagueis.weathersafe.viewmodel.UserAlbumViewModel;

import static productions.darthplagueis.weathersafe.util.CurrentDate.getDateString;
import static productions.darthplagueis.weathersafe.util.CurrentDate.getTimeStamp;

public class CreateAlbumDialog extends DialogFragment {

    private List<String> userAlbumList = new ArrayList<>();

    private View rootView;
    private TextInputLayout albumLayout, tagLayout;
    private TextInputEditText albumEditText, tagEditText;

    private String albumName;
    private String albumTag;

    private UserAlbumViewModel albumViewModel;

    private GalleryToolbarContract.presenter presenter;

    public static CreateAlbumDialog newInstance(GalleryToolbarContract.presenter presenter) {
        CreateAlbumDialog dialog = new CreateAlbumDialog();
        dialog.presenter = presenter;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setViews();

        albumViewModel = ViewModelProviders.of(this).get(UserAlbumViewModel.class);
        albumViewModel.getAllAlbums().observe(this, userAlbums -> {
            if (userAlbums != null) {
                for (UserAlbum item : userAlbums) {
                    userAlbumList.add(item.getDirectoryName());
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setView(rootView)
                .setTitle(R.string.album_dialog_title)
                .setMessage(R.string.album_dialog_message)
                .setPositiveButton(R.string.positive_btn_create, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        return alertDialog;
    }

    private void setViews() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.create_album_dialog, null, false);
        albumLayout = rootView.findViewById(R.id.dialog_text_album);
        tagLayout = rootView.findViewById(R.id.dialog_text_tag);
        albumEditText = rootView.findViewById(R.id.dialog_edit_album);
        tagEditText = rootView.findViewById(R.id.dialog_edit_tag);
        addTextWatcher();
    }

    private void onDialogShow(AlertDialog dialog) {
        Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(v -> onCreateClicked());
        Button cancelBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancelBtn.setOnClickListener(v1 -> dismiss());
    }

    private void onCreateClicked() {
        if (isValidName(albumLayout, albumName)) {
            UserAlbum userAlbum;
            if (!TextUtils.isEmpty(albumTag)) {
                userAlbum = new UserAlbum(albumName, albumTag, getDateString(), getTimeStamp());
            } else {
                userAlbum = new UserAlbum(albumName, getDateString(), getTimeStamp());
            }
            albumViewModel.insert(userAlbum);
            presenter.createNewAlbum(new FileManager(getContext()), userAlbum);
            dismiss();
        }
    }

    private boolean isValidName(TextInputLayout layout, String name) {
        if (TextUtils.isEmpty(name)) {
            layout.setErrorEnabled(true);
            layout.setError(getString(R.string.album_dialog_empty));
            return false;
        } else if (userAlbumList.contains(name)) {
            layout.setErrorEnabled(true);
            layout.setError(String.format(getString(R.string.create_dialog_exists_error), name));
            return false;
        }

        layout.setErrorEnabled(false);
        layout.setError("");
        return true;
    }

    private void addTextWatcher() {
        albumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                albumName = s.toString();
                tagLayout.setVisibility(View.VISIBLE);
            }
        });
        tagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                albumTag = s.toString();
            }
        });
    }
}
