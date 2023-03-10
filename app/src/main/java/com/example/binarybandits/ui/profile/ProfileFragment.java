package com.example.binarybandits.ui.profile;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.concurrent.CountDownLatch;

public class ProfileFragment extends Fragment {

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + AuthController.getUsername(getActivity());
        imageView = view.findViewById(R.id.profileIconImageView);
        Picasso.get().load(url).into(imageView);
        getCurrentPlayer(view);
        return view;
    }

    /**
     *
     */
    public void getCurrentPlayer(View view) {
        //I'm still debugging this method. Not ready to use
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = preferences.getString("login_username", "");
        PlayerDB db = new PlayerDB(new DBConnector());
        TextView usernameText = view.findViewById(R.id.text_username);
        usernameText.setText(username);
        db.getPlayer(username, new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player player) {
                TextView scoreText = view.findViewById(R.id.score_text);
                scoreText.setText(String.valueOf(player.getTotalScore()));

                TextView totalQRText = view.findViewById(R.id.total_qr_scanned_text);
                totalQRText.setText(String.valueOf(player.getTotalQRCodes()));
                //Get highest/lowest scoring QR codes

                //Get ListView of QR codes scanned
            }
        });
    }
}
