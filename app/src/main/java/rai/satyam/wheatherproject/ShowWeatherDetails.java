package rai.satyam.wheatherproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowWeatherDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowWeatherDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "lat";
    private static final String ARG_PARAM2 = "lon";

    // TODO: Rename and change types of parameters
    private String g_sLat;
    private String g_sLon;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_weather_details, container, false);
    }
}