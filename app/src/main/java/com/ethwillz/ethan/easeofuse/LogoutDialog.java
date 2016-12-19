package com.ethwillz.ethan.easeofuse;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import static android.support.v4.app.ActivityCompat.finishAffinity;

public class LogoutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View logoutDialog = layoutInflater.inflate(R.layout.logout_dialog, null);
        Button logout = (Button) logoutDialog.findViewById(R.id.logout);
        final Typeface main = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Walkway Bold.ttf");
        logout.setTypeface(main);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                finishAffinity(getActivity());
            }
        });
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(logoutDialog);
        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        return dialog;
    }
}