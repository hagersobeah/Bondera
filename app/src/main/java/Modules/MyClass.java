package Modules;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ea on 12/15/2017.
 */

public class MyClass {
    private static Context context;
    public MyClass(Context c) {
        context = c;
    }

    public static void showToastMethod() {
        Toast.makeText(context, "mymessage ", Toast.LENGTH_SHORT).show();
    }

}