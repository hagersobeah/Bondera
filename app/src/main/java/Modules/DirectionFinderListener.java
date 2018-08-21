package Modules;


import java.util.List;

/**
 * Created by ea on 12/11/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
