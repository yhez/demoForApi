package systems.useapi.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


public class FullscreenActivity extends Activity {
    EditText name, data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        name = (EditText) findViewById(R.id.name);
        data = (EditText) findViewById(R.id.data);
    }

    public void onClick(View v) {
        //hide keyboard
        View vf = getCurrentFocus();
        if (vf != null) {
            vf.clearFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vf.getWindowToken(), 0);
        }

        if (v.getId() == R.id.get) {
            SpecSafeHelper.getFileFromSafe(this, name.getText().toString());
        } else if (v.getId() == R.id.put) {
            SpecSafeHelper.saveFileInSafe(this, name.getText().toString(), data.getText().toString().getBytes());
        }
    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        if (res == RESULT_OK) {
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
            if (req == SpecSafeHelper.ACTION_GET) {
                byte[] result = intent.getByteArrayExtra("bytes");
                data.setText(new String(result));
            }
        } else {
            Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show();
            switch (res){
                case SpecSafeHelper.FAILED_MISSING_NAME:
                    break;
                case SpecSafeHelper.FAILED_CANT_FIND_ACTION:
                    break;
                case SpecSafeHelper.FAILED_CANT_FIND_FILE:
                    break;
                case SpecSafeHelper.FAILED_NO_PUBLIC:
                    break;
                case SpecSafeHelper.UNKNOWN_PROBLEM_WHILE_DECRYPTING:
                    break;
                case SpecSafeHelper.UNKNOWN_PROBLEM_WHILE_ENCRYPTING:
                    break;
            }
        }
    }
}
