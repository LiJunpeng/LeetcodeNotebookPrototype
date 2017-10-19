package com.example.louis.leetcodenotebook.activities.AddQuestionFragments;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.louis.leetcodenotebook.R;
import com.example.louis.leetcodenotebook.Server.AndroidWebServer;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Louis on 10/8/2017.
 */

public class FragmentServer extends Fragment {

    private static final int DEFAULT_PORT = 8080;

    ImageButton buttonToggleServer;
    TextView textViewIPLabel;

    private boolean isStarted;
    private AndroidWebServer androidWebServer;
    WifiManager wifiManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
//        textViewAnswer = (TextView) view.findViewById(R.id.textview_answer);
//        textViewAnswer.setText(questionData.get("answer"));
//        textViewAnswer.setTextIsSelectable(true);

        textViewIPLabel = (TextView) view.findViewById(R.id.textview_ip_label);
        textViewIPLabel.setText(getIpAccess() + DEFAULT_PORT);

        buttonToggleServer = (ImageButton) view.findViewById(R.id.button_toggle_server);
        buttonToggleServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStarted) {
                    stopServer();
//                    stopAndroidWebServer();
//                    System.out.println("Stoped");
//                    textViewIPLabel.setText("");
//                    isStarted = false;
//                    buttonToggleServer.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_info));
                } else {
                    startAndroidWebServer();
                    System.out.println(getIpAccess());
                    //textViewIPLabel.setText(getIpAccess());
                    isStarted = true;
                    buttonToggleServer.setImageDrawable(getResources().getDrawable(R.drawable.ic_server_stop));
                }
            }
        });

        return view;
    }

    //region Start And Stop AndroidWebServer
    private boolean startAndroidWebServer() {
        if (!isStarted) {
            //int port = getPortFromEditText();
            int port = 8080;
            try {
                if (port == 0) {
                    throw new Exception();
                }
                androidWebServer = new AndroidWebServer(port);
                androidWebServer.setContext(getContext()); // ???
                androidWebServer.start();
                System.out.println("Server start");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                //Snackbar.make(coordinatorLayout, "The PORT " + port + " doesn't work, please change it between 1000 and 9999.", Snackbar.LENGTH_LONG).show();
            }
        }
        return false;
    }

    private boolean stopAndroidWebServer() {
        if (isStarted && androidWebServer != null) {
            androidWebServer.stop();
            return true;
        }
        return false;
    }


    private String getIpAccess() {

        //WifiManager wifiManager = (WifiManager) getActivity(),getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return "http://" + formatedIpAddress + ":";
    }

    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public boolean isServerRunning() {
        return isStarted;
    }

    public void stopServer() {
        stopAndroidWebServer();
        System.out.println("Stoped");
        //textViewIPLabel.setText("");
        isStarted = false;
        buttonToggleServer.setImageDrawable(getResources().getDrawable(R.drawable.ic_server_start));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndroidWebServer();
        isStarted = false;
    }
}
