package mm.pndaza.mahabuddhavan.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.model.Book;
import mm.pndaza.mahabuddhavan.model.Note;
import mm.pndaza.mahabuddhavan.model.Toc;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static DBOpenHelper sInstance;
    private static final String DATABASE_PATH = "/databases/";
    private static final String DATABASE_NAME = "buddhavan.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = "DBOpenHelper";


    public static synchronized DBOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DBOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBOpenHelper(Context context) {
        super(context, context.getFilesDir() + DATABASE_PATH + DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public String getBookName(int bookId){
        String bookName = "";
        Cursor cursor = getReadableDatabase().rawQuery("SELECT name FROM book WHERE id = "
                + bookId , null);
        if (cursor != null && cursor.moveToFirst()) {
            bookName = cursor.getString(cursor.getColumnIndex("name"));
        }
        return bookName;
    }

    public ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        int id;
        String name;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT id, name FROM book", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                name = cursor.getString(cursor.getColumnIndex("name"));
                books.add(new Book(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }


    public ArrayList<Note> getNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        int id;
        String name;
        int volume;
        int page;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT rowid, name, volume, page FROM note", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("rowid"));
                name = cursor.getString(cursor.getColumnIndex("name"));
                volume = cursor.getInt(cursor.getColumnIndex("volume"));
                page = cursor.getInt(cursor.getColumnIndex("page"));
                notes.add(new Note(id, name, volume, page));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;

    }

    public ArrayList<Toc> getToc(int volume){
        ArrayList<Toc> tocs = new ArrayList<>();
        String name;
        int level;
        int page;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT name, level, page FROM toc where volume = " + volume, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("name"));
                level = cursor.getInt(cursor.getColumnIndex("level"));
                page = cursor.getInt(cursor.getColumnIndex("page"));
                tocs.add(new Toc(name, level, page));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tocs;

    }

    public int getRecentPage(int bookId){
        int page = 1;

        Cursor cursor = getReadableDatabase().rawQuery("SELECT page FROM recent WHERE volume = " + bookId, null);
        if (cursor != null && cursor.moveToFirst()) {
                page = cursor.getInt(cursor.getColumnIndex("page"));
        }
        cursor.close();

        return page;
    }

    public void setRecentPage(int bookId, int page){
        getWritableDatabase().execSQL("REPLACE INTO recent(volume, page) VALUES(?,?)",new Object[]{bookId,page});
    }

}
