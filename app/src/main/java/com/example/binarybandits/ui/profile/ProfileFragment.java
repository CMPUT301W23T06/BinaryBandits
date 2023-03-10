package com.example.binarybandits.ui.profile;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.player.PlayerDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + AuthController.getUsername(getActivity());
        imageView = view.findViewById(R.id.profileIconImageView);
        Picasso.get().load(url).into(imageView);
        getCurrentPlayer();
        return view;
    }

    /**
     *
     */
    public void getCurrentPlayer() {
        //I'm still debugging this method. Not ready to use
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = preferences.getString("login_username", "");
        PlayerDB db = new PlayerDB(new DBConnector());
        db.getPlayer(username, new PlayerDB.Callback() {
            @Override
            public void onCallback(Player player) {

            }
        }); //temporary
    }
}
