package com.example.razi.wikipediaclient.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.razi.wikipediaclient.R;

public class NetworkErrorDialog extends DialogFragment {

    private boolean shouldCallDismiss = true;

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (getActivity() instanceof NetworkErrorDialogListener) {
                NetworkErrorDialogListener networkErrorDialogListener =
                        (NetworkErrorDialogListener)getActivity();
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    networkErrorDialogListener.onConnect();
                    shouldCallDismiss = false;
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    networkErrorDialogListener.onDismiss();
                    shouldCallDismiss = false;
                }
            }
            dialog.dismiss();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(
                getString(R.string.dialog_network_error_title));
        builder.setMessage(
                getString(R.string.dialog_network_error_message));
        builder.setPositiveButton(
                getString(R.string.dialog_network_error_button_yes), listener);
        builder.setNegativeButton(
                getString(R.string.dialog_network_error_button_no), listener);
        return (builder.create());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (shouldCallDismiss && (getActivity() instanceof NetworkErrorDialogListener)) {
            NetworkErrorDialogListener networkErrorDialogListener =
                    (NetworkErrorDialogListener)getActivity();
            networkErrorDialogListener.onDismiss();
        }
    }

    public interface NetworkErrorDialogListener {
        void onConnect();
        void onDismiss();
    }
}
