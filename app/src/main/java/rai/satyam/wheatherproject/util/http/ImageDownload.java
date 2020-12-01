package rai.satyam.wheatherproject.util.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import rai.satyam.wheatherproject.R;

public class ImageDownload implements DownloadCallback<DownloadTaskResult> {



    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;
    ImageView imageView;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public void setNetworkFragment(NetworkFragment networkFragment) {
        this.networkFragment = networkFragment;
    }


    public ImageDownload() {
    }

    public static ImageDownload newInstance(FragmentManager fragmentManager, String url,ImageView imageView) {
        ImageDownload fragment = new ImageDownload();
        fragment.setImageView(imageView);
        fragment.setNetworkFragment(NetworkFragment.getInstance(fragmentManager, url ,fragment));
        fragment.startDownload();
        return fragment;
    }

    @Override
    public void updateFromDownload(DownloadTaskResult result) {
        try {
            if (result.getResponseStatus() == 2){
                imageView.setImageBitmap(result.getResponseImage());
            }
        } catch(Exception v_exException){
            v_exException.printStackTrace();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        /*ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;*/
        return null;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            networkFragment.startDownload();
            downloading = true;
        }
    }
}