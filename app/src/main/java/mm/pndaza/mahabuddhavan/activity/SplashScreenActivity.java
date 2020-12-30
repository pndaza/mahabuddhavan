package mm.pndaza.mahabuddhavan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.utils.SharePref;


public class SplashScreenActivity extends AppCompatActivity {

    private static final String DATABASE_PATH = "databases";
    private static final String DATABASE_FILENAME = "buddhavan.db";
    private String SAVED_PATH;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // super.onCreate မတိုင်မီ theme set လုပ်မှ အဆင်ပြေ။
        // မဟုတ်ရင် အောက်မှာ ပြထားတဲ့ error တချို့ဖုန်းမှာ တက်
        // E/ActivityInjector: get life cycle exception
        // java.lang.ClassCastException: android.os.BinderProxy cannot be cast to android.app.servertransaction.ClientTransaction

        // load theme
        SharePref sharePref = SharePref.getInstance(this);
        if (SharePref.getInstance(this).getPrefNightModeState() == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SAVED_PATH = getFilesDir().toString();
        context = this;

        // save default setting

        if (sharePref.isFirstTime()) {
            sharePref.saveDefault();
        }

        if (isDatabaseExist() && sharePref.isDatabaseCopied()) {
            startMainActivity();
        } else {
            setupDatabase();
        }

    }

    private boolean isDatabaseExist() {
        return new File(SAVED_PATH + "/" + DATABASE_PATH + "/" + DATABASE_FILENAME).exists();
    }

    private void setupDatabase() {

        new CopyDBAsync().execute();
    }

    public class CopyDBAsync extends AsyncTask<Void, Void, Void> {


        protected Void doInBackground(Void... voids) {

            File path = new File(SAVED_PATH + "/" + DATABASE_PATH );
            // check database folder is exist and if not, make folder.
            if (!path.exists()) {
                path.mkdirs();
            }

            try {
                InputStream inputStream = getAssets()
                        .open(DATABASE_PATH + "/" + DATABASE_FILENAME);
                OutputStream outputStream = new FileOutputStream(
                        SAVED_PATH + "/" + DATABASE_PATH + "/" + DATABASE_FILENAME);

                byte[] buffer = new byte[1024];
                int length;
                while (( length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            SharePref.getInstance(context).setDbCopyState(true);
            startMainActivity();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private void startMainActivity() {

        new Handler().postDelayed(() -> {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }, 1000);

    }

}
