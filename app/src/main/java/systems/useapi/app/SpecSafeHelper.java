package systems.useapi.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

public class SpecSafeHelper {
    /*failed reasons*/
    public final static int
            //can't get a name for the file
            FAILED_MISSING_NAME=11,
            //can't find a proper action to execute
            FAILED_CANT_FIND_ACTION=12,
            //the file requested doesn't exist
            FAILED_CANT_FIND_FILE=13,
            //can't find a public key in spec,
            //that is indicate that the user has probably not create an account yet.
            //try start spec for creating an account.
            FAILED_NO_PUBLIC=14,
            UNKNOWN_PROBLEM_WHILE_ENCRYPTING=15,
            UNKNOWN_PROBLEM_WHILE_DECRYPTING=16;
    public final static int ACTION_PUT = 1, ACTION_GET = 2;

    //the name is used to get back the file afterwards
    //the byte array is the actually data to encrypt
    public static void saveFileInSafe(Activity activity, String nameOfFile, byte[] data) {
        Intent i = new Intent();
        i.putExtra("action", ACTION_PUT);
        i.putExtra("bytes", data);
        i.putExtra("name", nameOfFile);
        start(i, activity);
    }

    public static void getFileFromSafe(Activity activity, String nameOfFile) {
        Intent i = new Intent();
        i.putExtra("action", ACTION_GET);
        i.putExtra("name", nameOfFile);
        start(i, activity);
    }

    private static void start(Intent i, Activity activity) {
        String pc = "specular.systems";
        String ac = "specular.systems.activities.SpecSafe";
        ComponentName cn = new ComponentName(pc, ac);
        i.setComponent(cn);
        try {
            activity.startActivityForResult(i, i.getIntExtra("action",0));
        } catch (Exception ignore) {
            ignore.printStackTrace();
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pc)));
            } catch (Exception e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + pc)));
            }
        }
    }
}
