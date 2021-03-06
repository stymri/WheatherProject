package rai.satyam.wheatherproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import rai.satyam.wheatherproject.util.Utils;
import rai.satyam.wheatherproject.util.http.DownloadCallback;
import rai.satyam.wheatherproject.util.http.DownloadTaskResult;
import rai.satyam.wheatherproject.util.http.ImageDownload;
import rai.satyam.wheatherproject.util.http.NetworkFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowWeatherDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowWeatherDetails extends Fragment implements DownloadCallback<DownloadTaskResult> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "lat";
    private static final String ARG_PARAM2 = "lon";

    // TODO: Rename and change types of parameters
    private String g_sLat;
    private String g_sLon;

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;

    TextView textViewDate;
    TextView textViewTemp;
    ImageView imageView;
    TextView textViewWheather;

    public ShowWeatherDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowWeatherDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowWeatherDetails newInstance(String param1, String param2) {
        ShowWeatherDetails fragment = new ShowWeatherDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            g_sLat = getArguments().getString(ARG_PARAM1);
            g_sLon = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        networkFragment = NetworkFragment.getInstance(getActivity().getSupportFragmentManager(), "https://api.openweathermap.org/data/2.5/onecall?lat=28.67&lon=77.22&units=metric&appid=154b075c1652f4f2d9c3b7420ab1362a" ,this);
        this.startDownload();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_weather_details, container, false);
    }

    @Override
    public void updateFromDownload(DownloadTaskResult result) {
        String v_sResponse , v_sTemp;
        JSONObject v_objJson , v_objJsonCurrent ,v_objJsonWheather;
        JSONArray v_objJsonArray;
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        ImageDownload imageDownload;
        try {
            textViewDate = getActivity().findViewById(R.id.textViewDate);
            textViewTemp = getActivity().findViewById(R.id.textViewTemp);
            imageView = getActivity().findViewById(R.id.imageView);
            textViewWheather = getActivity().findViewById(R.id.textViewWheather);
            if (result.getResponseString() != null){
                v_sResponse = result.getResponseString();
                //Log.d("response",v_sResponse);
                v_objJson = new JSONObject(v_sResponse);
                v_objJsonCurrent = v_objJson.getJSONObject("current");
                v_sTemp = dateFormat.format("hh:mm:ss a",new Date(v_objJsonCurrent.getLong("dt")*1000L)).toString();
                textViewDate.setText(v_sTemp);
                v_sTemp = "" + v_objJsonCurrent.getDouble("temp") + " °C";
                textViewTemp.setText(v_sTemp);
                v_objJsonArray = v_objJsonCurrent.getJSONArray("weather");
                v_objJsonWheather = v_objJsonArray.getJSONObject(0);
                v_sTemp = v_objJsonWheather.getString("description");
                textViewWheather.setText(v_sTemp);
                v_sTemp = "https://openweathermap.org/img/wn/"+v_objJsonWheather.getString("icon")+"@4x.png";
                imageDownload = ImageDownload.newInstance(getActivity().getSupportFragmentManager() ,v_sTemp,imageView);

            }
        } catch(Exception v_exException){
            v_exException.printStackTrace();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
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