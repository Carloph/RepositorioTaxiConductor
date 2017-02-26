package com.taxiconductor.DirectionMaps;

/**
 * Created by carlos on 25/02/17.
 */
import android.content.Intent;

import java.util.List;

/**
 * Created by carlos on 22/02/17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);

    void startActivityForResult(int requestCode, int resultCode, Intent data);
}