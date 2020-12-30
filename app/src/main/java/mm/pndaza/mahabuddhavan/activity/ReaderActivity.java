package mm.pndaza.mahabuddhavan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.database.DBOpenHelper;
import mm.pndaza.mahabuddhavan.fragment.TocDialogFragment;
import mm.pndaza.mahabuddhavan.model.Book;
import mm.pndaza.mahabuddhavan.utils.MDetect;
import mm.pndaza.mahabuddhavan.utils.SharePref;

public class ReaderActivity extends AppCompatActivity implements TocDialogFragment.TocDialogListener {

    private static final String TAG = "ReaderActivity";
    private Book book;
    private PDFView pdfView;
    private String filename;

    private boolean nightMode = false;
    private boolean actionVisibleState = true;
    private boolean isVertical = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        getSupportActionBar();
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        book = intent.getParcelableExtra("book");
        int pageNumber = intent.getIntExtra("page", 1);
        Log.d(TAG, "onCreate: pageNumber " + pageNumber);

        MDetect.init(this);

        setTitle(MDetect.getDeviceEncodedText(book.getName()));
        filename = "books" + File.separator + book.getId() + ".pdf";


        if (SharePref.getInstance(this).getPrefNightModeState() == 2) {
            nightMode = true;
        }

        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset(filename)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .defaultPage(pageNumber - 1)
                .scrollHandle(new DefaultScrollHandle(this))
                .pageFitPolicy(FitPolicy.WIDTH)
                .nightMode(nightMode)
                .load();

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionVisibleState) {
                    getSupportActionBar().hide();
                    actionVisibleState = !actionVisibleState;
                } else {
                    getSupportActionBar().show();
                    actionVisibleState = !actionVisibleState;
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        saveToRecent(book.getId(), pdfView.getCurrentPage() + 1);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveToRecent(book.getId(), pdfView.getCurrentPage() + 1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reader, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_scroll_mode:
                swapIcon(item);
                changePageMode();
                return true;
            case R.id.menu_toc:
                showToc();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void swapIcon(MenuItem item){
        if( isVertical) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_swap_horiz_24));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_swap_vert_24));
        }

    }
    private void changePageMode(){

        int currentPage = pdfView.getCurrentPage();
        if( isVertical) {
            pdfView.fromAsset(filename)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFling(true)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .defaultPage(currentPage)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .nightMode(nightMode)
                    .load();
            isVertical = !isVertical;
        } else {
            pdfView.fromAsset(filename)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .defaultPage(currentPage)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(nightMode)
                    .load();
            isVertical = !isVertical;
        }

    }
    private void showToc() {
        Bundle args = new Bundle();
        args.putInt("volume", book.getId());

        FragmentManager fm = getSupportFragmentManager();
        TocDialogFragment tocDialogFragment = new TocDialogFragment();
        tocDialogFragment.setArguments(args);
        tocDialogFragment.show(fm, "toc");
    }

    private void saveToRecent(int volume, int page){
        DBOpenHelper.getInstance(this).setRecentPage(volume, page);
    }

    @Override
    public void onTocItemClick(int page) {
        pdfView.jumpTo(page - 1);
    }
}
