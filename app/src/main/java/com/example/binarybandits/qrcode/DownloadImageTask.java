package com.example.binarybandits.qrcode;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;


/**
 * Retrieves images using dicebear based on a given string
 */
public class DownloadImageTask {

    /**
     * Load an avatar into a given ImageView by getting the url of the username and loading
     * the image corresponding to the url into the ImageView
     * @param imageView ImageView to load an image into
     * @param username player's username
     */
    public static void loadAvatarImageIntoView(ImageView imageView, String username) {
        String url = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + username;
        Picasso.get().load(url).fit().centerCrop().into(imageView);
    }

    /**
     * Load the visual representation of a QR code into a given ImageView by getting the url of the
     * hash and loading the image corresponding to the url into the ImageView
     * @param imageView ImageView to load an image into
     * @param hash QRCode hash
     */
    public static void loadQRImageIntoView(ImageView imageView, String hash) {
        String url = "https://api.dicebear.com/5.x/shapes/png?seed=" + hash;
        Picasso.get().load(url).fit().centerCrop().into(imageView);
    }

}
