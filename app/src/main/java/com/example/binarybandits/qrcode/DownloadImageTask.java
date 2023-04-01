package com.example.binarybandits.qrcode;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;


/**
 * Retrieves images using dicebear based on a given string
 */
public class DownloadImageTask {

    /**
     *
     * @param imageView
     * @param username
     */
    public static void loadAvatarImageIntoView(ImageView imageView, String username) {
        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + username;
        Picasso.get().load(url).fit().centerCrop().into(imageView);
    }

    /**
     *
     * @param imageView
     * @param hash
     */
    public static void loadQRImageIntoView(ImageView imageView, String hash) {
        String url = "https://api.dicebear.com/5.x/shapes/png?seed=" + hash;
        Picasso.get().load(url).fit().centerCrop().into(imageView);
    }

}
