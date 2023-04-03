package com.example.binarybandits.qrcode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.binarybandits.R;
import com.example.binarybandits.models.QRCode;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Class that links the app UI to a list of QR codes.
 */
public class QRArrayAdapter extends ArrayAdapter<QRCode> {
    /**
     * Constructor for QRArrayAdapter
     * @param context
     * @param qrCodes list of QRCode objects
     */
    public QRArrayAdapter(Context context, ArrayList<QRCode> qrCodes) {
        super(context, 0, qrCodes);
    }

    /**
     * Set the name, score, and picture of each QR code in the list of QR codes
     * @param position
     * @param convertView
     * @param parent
     * @return Return the resulting view of setting each entry in the array adapter
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //View view;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.qr_item, parent, false);
        }
        else {
            convertView = convertView;
        }

        QRCode qr = getItem(position);

        TextView QR_name_text = convertView.findViewById(R.id.QR_name);
        TextView QR_score_text = convertView.findViewById(R.id.QR_score);
        ImageView QR_pic_display = convertView.findViewById(R.id.QR_pic);

        String url = qr.getImageURL();
        Picasso.get().load(url).into(QR_pic_display);

        QR_name_text.setText(qr.getName());
        QR_score_text.setText(Integer.toString(qr.getPoints()));

        //QR_pic_display.setImageResource();
        //


        return convertView;
    }
}
