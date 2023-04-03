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

/**
 *
 */
public class QReditFragment extends Fragment{

    private QReditViewModel mViewModel;
    private int REQUEST_IMAGE_CAPTURE = 1 ;
    private Button add_location;

    /**
     *
     * @return
     */
    public static QReditFragment newInstance() {
        return new QReditFragment();
    }

    /**
     *
     * @param inflater
     * @param container
     * @param v
     */
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

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * Creates the QRCodeEditFragmentView
     * @param inflater the layout inflater
     * @param container the view group container
     * @param savedInstanceState the saved instance state that is restored after the app crashes
     * @return Return the created QRCodeEditFragment view java object
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qredit, container, false);
    }

    /**
     * Contains code determining what to do when the activity is created.
     * @param savedInstanceState the saved instance state that is restored after the app crashes
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QReditViewModel.class);
        // TODO: Use the ViewModel
    }

}