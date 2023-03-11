package com.example.binarybandits.ui.QRedit;

import static android.app.Activity.RESULT_OK;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.binarybandits.R;

public class QReditFragment extends Fragment {

    private QReditViewModel mViewModel;
    private int REQUEST_IMAGE_CAPTURE = 1 ;
    private Button add_location;


    public static QReditFragment newInstance() {
        return new QReditFragment();
    }

    public void onClick(LayoutInflater inflater, ViewGroup container ,View v) {

        if ( v == getView().findViewById(R.id.add_location_button)) {
            inflater.inflate(R.layout.fragment_qredit, container, false);
            add_location = getView().findViewById(R.id.add_location_button);
            add_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            });

        }
        else if(v== getView().findViewById(R.id.skip_button)) {
            View rootview = inflater.inflate(R.layout.activity_main,container,false);

        }

    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap capturedImage = (Bitmap) extras.get("data");

            // TODO: Implement the logic to save the captured image to storage, display it in the UI, etc.

            // Example of displaying the captured image in an ImageView
            ImageView imageView = getView().findViewById(R.id.add_location_button);
            imageView.setImageBitmap(capturedImage);

        }}
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked) {
                    // TODO Record the geolocation

                }

                break;

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qredit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QReditViewModel.class);
        // TODO: Use the ViewModel
    }

}