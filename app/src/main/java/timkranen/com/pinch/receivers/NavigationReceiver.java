package timkranen.com.pinch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.HashMap;

import timkranen.com.pinch.controllers.DetailController;
import timkranen.com.pinch.controllers.MainController;

/**
 * @author tim on [11/12/16]
 *
 * Broadcast receiver that handles navigational changes
 */
public class NavigationReceiver extends BroadcastReceiver {
    private static final String TAG = NavigationReceiver.class.getSimpleName();

    public static final String ACTION_NAVIGATE_TO = TAG + ".actionNavigateTo";
    public static final String EXTRA_CONTROLLER_ID = "controllerId";

    private Router _router;

    public NavigationReceiver(Router router) {
        _router = router;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction().equals(ACTION_NAVIGATE_TO)) {
            navigate(intent.getIntExtra(EXTRA_CONTROLLER_ID, -1));
        }
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAVIGATE_TO);
        return intentFilter;
    }

    private void navigate(int controllerID) {
        if(controllerID != -1) {
            Controller targetController = new NavigationMap().get(controllerID);
            _router.pushController(RouterTransaction.builder(targetController).build());
        }
    }

    public static class NavigationMap extends HashMap<Integer, Controller> {
        public NavigationMap() {
            put(MainController.CONTROLLER_ID, new MainController());
            put(DetailController.CONTROLLER_ID, new DetailController());
        }
    }
}
