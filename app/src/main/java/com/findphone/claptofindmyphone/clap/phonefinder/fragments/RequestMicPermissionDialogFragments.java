package com.findphone.claptofindmyphone.clap.phonefinder.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.findphone.claptofindmyphone.clap.phonefinder.R;
import com.findphone.claptofindmyphone.clap.phonefinder.ui.utils.Permissions;


public class RequestMicPermissionDialogFragments extends DialogFragment {
    AppCompatButton btn_allow_permission;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        if (onCreateDialog.getWindow() != null) {
            onCreateDialog.getWindow().requestFeature(1);
        }
        return onCreateDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null || dialog.getWindow() == null) {
            return;
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.request_mic_permisson_dialog_fragment, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        AppCompatButton appCompatButton = (AppCompatButton) view.findViewById(R.id.btn_allow_permission);
        this.btn_allow_permission = appCompatButton;
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                RequestMicPermissionDialogFragments.this.dismiss();
                if (RequestMicPermissionDialogFragments.this.getActivity() != null) {
                    Permissions.requestMicPermission((AppCompatActivity) RequestMicPermissionDialogFragments.this.getActivity(), Permissions.MIC_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }
}
