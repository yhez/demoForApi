package systems.useapi.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FullscreenActivity extends Activity {
    EditText name, data;TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        name = (EditText) findViewById(R.id.name);
        data = (EditText) findViewById(R.id.data);
        result = (TextView)findViewById(R.id.result);
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
        }else if(v.getId()==R.id.get_files){
            SpecSafeHelper.getListFile(this);
        }else if(v.getId()==R.id.choose){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            Intent i = Intent.createChooser(intent, getString(R.string.choose_file));
            startActivityForResult(i, 99);
        }
    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        if (res == RESULT_OK) {
            if(req==99){
                data.setText(intent.getData().getPath());
                name.setText(intent.getData().getLastPathSegment());
                return;
            }
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
            if (req == SpecSafeHelper.ACTION_GET) {
                byte[] r = intent.getByteArrayExtra("bytes");
                result.setText(new String(r));
            }else if(req==SpecSafeHelper.ACTION_GET_ALL){
                String[] r = intent.getStringArrayExtra("files");
                String s="";
                for(String st:r)
                    s += st+"\n";
                result.setText(s);
            }else if(req==SpecSafeHelper.ACTION_PUT){
                data.getText().clear();
                name.getText().clear();
            }
        } else {
            if(req!=99)
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
