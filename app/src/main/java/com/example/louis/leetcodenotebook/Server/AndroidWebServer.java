package com.example.louis.leetcodenotebook.Server;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Louis on 10/15/2017.
 */

public class AndroidWebServer extends NanoHTTPD {

    private Context context;

    public AndroidWebServer(int port) {
        super(port);
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();
        if (parms.get("title") == null) {
            msg += "<form action='?' method='get'>\n";
            msg += "<p>Title: <input type='text' name='title'></p>\n";
            //msg += "<p>Description: <input type='text' name='description'></p>\n";
            msg += "<p>Description <textarea name='description' rows='80' cols='200' ></textarea></p>\n";
            msg += "<input type='submit' value='Submit' />";
            msg += "</form>\n";
        } else {
            //msg += "<p>Hello, " + parms.get("title") + "!</p>";
            msg += "<form action='?' method='get'>\n";
            msg += "<input type='submit' value='New Question' />";
            msg += "</form>\n";

            System.out.println(parms.get("title"));
            System.out.println(parms.get("description"));



        }
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }


}
