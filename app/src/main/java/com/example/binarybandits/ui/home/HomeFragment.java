package com.example.binarybandits.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import com.example.binarybandits.R;
import com.example.binarybandits.ScanQRActivity;
import com.example.binarybandits.controllers.PermissionsController;

public class HomeFragment extends Fragment {

    private ImageButton scanButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scanButton = view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                Intent myIntent = new Intent(getActivity(), ScanQRActivity.class);
                // myIntent.putExtra("key", value); // Optional parameters
                requireActivity().startActivity(myIntent);
            }
        });

        PermissionsController.askAllPermissions(getActivity());

        return view;
    }

}
