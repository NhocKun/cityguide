package com.kun.cityguide.util;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kun.cityguide.R;

/**
 * Created by kunmyt on 4/20/16.
 */
public class Common {

    public static boolean checkGooglePlayService(Context context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        Toast.makeText(context, context.getString(R.string.common_google_play_services_unknown_issue), Toast.LENGTH_SHORT).show();
        return status == ConnectionResult.SUCCESS;
    }
}
