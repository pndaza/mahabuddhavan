package mm.pndaza.mahabuddhavan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.model.Note;
import mm.pndaza.mahabuddhavan.utils.MDetect;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private OnItemClickListener onItemClickListener;
    private ArrayList<Note> notes;

    public NoteAdapter(ArrayList<Note> notes, OnItemClickListener onItemClickListener) {
        this.notes = notes;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name.setText(MDetect.getDeviceEncodedText(notes.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        char firstChar = notes.get(position).getName().charAt(0);
        return String.valueOf(firstChar);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(notes.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
}
