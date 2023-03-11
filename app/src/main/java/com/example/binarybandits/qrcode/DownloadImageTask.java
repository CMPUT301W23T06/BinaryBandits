package com.example.binarybandits.qrcode;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class DownloadImageTask {

    public static void loadAvatarImageIntoView(ImageView imageView, String username) {
        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + username;
        Picasso.get().load(url).fit().centerCrop().into(imageView);
    }

    public static void loadQRImageIntoView(ImageView imageView, String hash) {
        String url = "https://api.dicebear.com/5.x/shapes/png?seed=" + hash;
        Picasso.get().load(url).fit().centerCrop().into(imageView);
    }

}
