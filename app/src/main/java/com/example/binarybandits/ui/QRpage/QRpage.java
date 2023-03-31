package com.example.binarybandits.ui.QRpage;

import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.binarybandits.R;

import java.util.ArrayList;

public class QRpage extends Fragment {

    private QRpageViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View qrPage = inflater.inflate(R.layout.fragment_qrpage, container, false);
        ListView commentsView = qrPage.findViewById(R.id.commentsList);
        ArrayList commentsList = new ArrayList<>();
        ArrayAdapter commentsAdapter = new ArrayAdapter<>(getActivity(), R.layout.comment, commentsList);
        Log.d(TAG, "Yep not working for some reason");

        commentsList.add("this is a comment");
        commentsView.setAdapter(commentsAdapter);
        return qrPage;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QRpageViewModel.class);
        // TODO: Use the ViewModel
    }

}