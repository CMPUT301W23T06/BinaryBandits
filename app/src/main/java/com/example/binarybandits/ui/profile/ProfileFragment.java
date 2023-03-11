package com.example.binarybandits.ui.profile;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.MainActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.ScanQRActivity;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRArrayAdapter;
import com.example.binarybandits.qrcode.QRCodeCallback;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.ui.QRpage.QRpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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




//        for (int i = 0; i < 10; i++) {
//            dataList.add(new QRCode("hash", "name", 50));
//        }

//        db.getPlayer(username, new PlayerCallback() {
//            @Override
//            public void onPlayerCallback(Player player) {
//                ListView QRlist = view.findViewById(R.id.list_view_player_qr_codes);
//                ArrayList<QRCode> dataList = new ArrayList<>();
//                dataList = player.getQrCodesScanned();
//                ArrayAdapter<QRCode> QRAdapter = new QRArrayAdapter(getActivity(), dataList);
//                QRlist.setAdapter(QRAdapter);
//            }
//
//
//        });




        db.getPlayer(username, new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player player) {
                if (player != null) {
                    TextView scoreText = view.findViewById(R.id.score_text);
                    scoreText.setText(String.valueOf(player.getTotalScore()));

                    TextView totalQRText = view.findViewById(R.id.total_qr_scanned_text);
                    totalQRText.setText(String.valueOf(player.getTotalQRCodes()));

                    //Get highest/lowest scoring QR codes



                    //Get ListView of QR codes scanned

                    ListView QRlist = view.findViewById(R.id.list_view_player_qr_codes);
                    ArrayList<QRCode> dataList = new ArrayList<>();
                    dataList = player.getQrCodesScanned();
                    ArrayAdapter<QRCode> QRAdapter = new QRArrayAdapter(getActivity(), dataList);
                    QRlist.setAdapter(QRAdapter);


                    ArrayList<QRCode> finalDataList = dataList;
                    QRlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            Intent myIntent = new Intent(getActivity(), QRCodeInfoActivity.class);
                            myIntent.putExtra("name", String.valueOf(finalDataList.get(position).getName()));
                            getActivity().startActivity(myIntent);

                        }
                    });
                }
            }
        });


    }
}
