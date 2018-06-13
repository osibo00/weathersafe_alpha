package productions.darthplagueis.weathersafe.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import java.util.Objects;

import productions.darthplagueis.weathersafe.GalleryToolbarContract;
import productions.darthplagueis.weathersafe.GalleryToolbarPresenter;
import productions.darthplagueis.weathersafe.R;
import productions.darthplagueis.weathersafe.util.FileManager;

public class DeleteDialog extends DialogFragment {

    private GalleryToolbarContract.presenter presenter;

    public static DeleteDialog newInstance() {
        DeleteDialog dialog = new DeleteDialog();
        dialog.presenter = GalleryToolbarPresenter.getINSTANCE();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.dialog_yes, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        return alertDialog;
    }

    private void onDialogShow(AlertDialog dialog) {
        Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveBtn.setOnClickListener(v -> onYesCLicked());
        Button cancelBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancelBtn.setOnClickListener(v1 -> dismiss());
    }

    private void onYesCLicked() {
        presenter.delete(new FileManager(getContext()));
        dismiss();
    }
}
