package com.example.binarybandits.ui.profile;
import static android.graphics.Bitmap.Config.ARGB_8888;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ArrayList<QRCode> QRlist = new ArrayList<>();

        Player player = new Player("test user", "780", 2000,
                10, Bitmap.createBitmap(1,1,ARGB_8888), QRlist);

        //QRCode qr1 = new QRCode('hash', 'name', 100, 'scan',


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + AuthController.getUsername(getActivity());
        imageView = view.findViewById(R.id.profileIconImageView);
        Picasso.get().load(url).into(imageView);

        TextView username_text  = view.findViewById(R.id.text_username);
        TextView score_text = view.findViewById(R.id.score_label);
        TextView total_text = view.findViewById(R.id.total_qr_scanned_label);


        String username_string = AuthController.getUsername(getActivity());
        // playerDB.getPlayer()

        Integer score_string = player.getTotalScore();
        Integer scanned_string = player.getTotalQRCodes();



        username_text.setText(username_string);
        score_text.setText(Integer.toString(score_string));
        total_text.setText(Integer.toString(scanned_string));

        

        return view;
    }
}
