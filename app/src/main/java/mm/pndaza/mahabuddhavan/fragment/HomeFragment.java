package mm.pndaza.mahabuddhavan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.Adapter.BookAdapter;
import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.database.DBOpenHelper;
import mm.pndaza.mahabuddhavan.model.Book;
import mm.pndaza.mahabuddhavan.utils.MDetect;

public class HomeFragment extends Fragment implements BookAdapter.OnItemClickListener{

    private Context context;
    private OnBookChoiceListener onBookChoiceListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getContext();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(MDetect.getDeviceEncodedText(getString(R.string.app_name_mm)));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListView(view);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onBookChoiceListener = (OnBookChoiceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemented OnBookChoiceListener");

        }
    }

    private void initListView(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        ArrayList<Book> books = DBOpenHelper.getInstance(context).getBooks();
        BookAdapter adapter = new BookAdapter(books, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, getGridCount()));

    }


    public interface OnBookChoiceListener{
        void onBookChoice(Book book);
    }


    @Override
    public void onItemClick(Book book) {
        onBookChoiceListener.onBookChoice(book);
    }

    private int getGridCount(){
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            return 3;
        } else {
            return 2;
        }
    }
}
