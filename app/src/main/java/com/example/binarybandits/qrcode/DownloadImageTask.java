package com.example.binarybandits.qrcode;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class DownloadImageTask {

    public void loadImageIntoView(ImageView imageView, String username) {
        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + username;
        Picasso.get().load(url).into(imageView);
    }

}
