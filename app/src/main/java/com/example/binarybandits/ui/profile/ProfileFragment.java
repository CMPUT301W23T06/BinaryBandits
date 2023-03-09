package com.example.binarybandits.ui.profile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + AuthController.getUsername(getActivity());
        imageView = view.findViewById(R.id.profileIconImageView);
        Picasso.get().load(url).into(imageView);

        return view;
    }
}
