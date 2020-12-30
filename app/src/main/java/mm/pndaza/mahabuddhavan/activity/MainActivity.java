package mm.pndaza.mahabuddhavan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.database.DBOpenHelper;
import mm.pndaza.mahabuddhavan.fragment.HomeFragment;
import mm.pndaza.mahabuddhavan.fragment.SearchFragment;
import mm.pndaza.mahabuddhavan.fragment.SettingFragment;
import mm.pndaza.mahabuddhavan.model.Book;
import mm.pndaza.mahabuddhavan.model.Note;
import mm.pndaza.mahabuddhavan.utils.MDetect;
import mm.pndaza.mahabuddhavan.utils.SharePref;


public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnBookChoiceListener,
        SearchFragment.OnNoteClickListener
        , SettingFragment.OnSettingChangeListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharePref sharePref = SharePref.getInstance(this);
        if (sharePref.getPrefNightModeState() == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MDetect.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(MDetect.getDeviceEncodedText(getString(R.string.app_name)));

//        setTitle(MDetect.getDeviceEncodedText(getString(R.string.app_name)));

        // load theme
        if (SharePref.getInstance(this).getPrefNightModeState() == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

//        openFragment(new HomeFragment());
        if (savedInstanceState == null) {
            openFragment(new HomeFragment());
        }

        BottomNavigationView navView = findViewById(R.id.bottom_navigation_view);

        navView.setOnNavigationItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            switch ((item.getItemId())) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.navigation_setting:
                    selectedFragment = new SettingFragment();
                    break;
            }
            openFragment(selectedFragment);
            return true;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void openFragment(Fragment selectedFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, selectedFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBookChoice(Book book) {

        Intent intent = new Intent(this, ReaderActivity.class);
        intent.putExtra("book", book);
        int page = DBOpenHelper.getInstance(this).getRecentPage(book.getId());
        Log.d(TAG, "onBookChoice: pageNumber" + page);
        intent.putExtra("page", page);
        startActivity(intent);
    }

    @Override
    public void onChangeListener() {
        recreate();
    }

    @Override
    public void onNoteClick(Note note) {
        int bookId = note.getVolume();
        String bookName = DBOpenHelper.getInstance(this).getBookName(bookId);
        Book book = new Book(note.getVolume(), bookName);

        Intent intent = new Intent(this, ReaderActivity.class);
        intent.putExtra("book", book);
        intent.putExtra("page", note.getPage());
        startActivity(intent);

    }


}
