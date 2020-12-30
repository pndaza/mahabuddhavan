package mm.pndaza.mahabuddhavan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.model.Book;
import mm.pndaza.mahabuddhavan.utils.MDetect;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private ArrayList<Book> books;

    public BookAdapter(ArrayList<Book> books, OnItemClickListener onItemClickListener) {
        this.books = books;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cover.setImageResource(getResId("book_cover_" + books.get(position).getId() , R.drawable.class));
        holder.tv_name.setText(MDetect.getDeviceEncodedText(books.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cover;
        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            tv_name = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(books.get(getAdapterPosition()));
        }
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }
}
