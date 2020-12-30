package mm.pndaza.mahabuddhavan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.Adapter.NoteAdapter;
import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.database.DBOpenHelper;
import mm.pndaza.mahabuddhavan.model.Note;
import mm.pndaza.mahabuddhavan.utils.MDetect;
import mm.pndaza.mahabuddhavan.utils.Rabbit;

public class SearchFragment extends Fragment implements NoteAdapter.OnItemClickListener {

    private ArrayList<Note> all_note = new ArrayList<>();
    private ArrayList<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;
    private SearchView searchInput;
    private static Context context;
    private RecyclerView recyclerView;

    private OnNoteClickListener listener;

//    private static final String TAG = "BookSearchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(MDetect.getDeviceEncodedText(getString(R.string.note)));
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        MDetect.init(context);


        recyclerView = view.findViewById(R.id.recycler_view);
        all_note.addAll(DBOpenHelper.getInstance(context).getNotes());
        notes.addAll(all_note);
        adapter = new NoteAdapter(notes, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));


        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doFilter(newText);

                return false;
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnNoteClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemented OnNoteClickListener");

        }
    }

    private void doFilter(String filterWord) {
        notes.clear();
        if (filterWord.isEmpty()) {
            notes.addAll(all_note);
            adapter.notifyDataSetChanged();
        } else {
            if (!MDetect.isUnicode()) {
                filterWord = Rabbit.zg2uni(filterWord);
            }
            for (Note note : all_note) {
                if (note.getName().contains(filterWord)) {
                    notes.add(note);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(Note note) {
        listener.onNoteClick(note);
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }
}
