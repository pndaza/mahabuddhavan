package mm.pndaza.mahabuddhavan.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.Adapter.TocAdapter;
import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.database.DBOpenHelper;
import mm.pndaza.mahabuddhavan.model.Toc;


public class TocDialogFragment extends DialogFragment implements TocAdapter.OnItemClickListener {


    private TocDialogListener listener;
    private RecyclerView recyclerView;


    public interface TocDialogListener {

        void onTocItemClick(int page);
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_toc, container, false);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof TocDialogListener) {
            listener = (TocDialogListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement GotoDialogFragment.GotoDialogListener");
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        int volume = 1;
        Bundle args = getArguments();
        if (args != null) {
            volume = args.getInt("volume");
        }

        ArrayList<Toc> tocs = DBOpenHelper.getInstance(getContext()).getToc(volume);


        Log.d("TOC Dialog", "toclist size is " + tocs.size());
        TocAdapter adapter = new TocAdapter(tocs, this);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


    }

    @Override
    public void onItemClick(Toc toc) {
        listener.onTocItemClick(toc.getPage());
        dismiss();
    }
}
