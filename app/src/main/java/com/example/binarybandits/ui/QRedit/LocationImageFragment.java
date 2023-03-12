package com.example.binarybandits.ui.QRedit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.binarybandits.R;

public class LocationImageFragment extends DialogFragment {

    public LocationImageFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;
    private ImageView imageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        assert getTag() != null;
        View view = getLayoutInflater().inflate(R.layout.fragment_image_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap(this.bitmap);
        imageView.setRotation(90);

        builder.setView(view);
        builder.setNegativeButton("Done", null);

        return builder.create();
    }


}
