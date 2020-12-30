package mm.pndaza.mahabuddhavan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mm.pndaza.mahabuddhavan.R;
import mm.pndaza.mahabuddhavan.model.Toc;
import mm.pndaza.mahabuddhavan.utils.MDetect;

public class TocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TOC_LEVEl_ONE = 0;
    private static final int TOC_LEVEl_TWO = 1;
    private static final int TOC_LEVEl_THREE = 2;

    private OnItemClickListener onItemClickListener;
    private ArrayList<Toc> tocs;

    public TocAdapter(ArrayList<Toc> tocs, OnItemClickListener onItemClickListener) {
        this.tocs = tocs;
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public int getItemViewType(int position) {

        Toc toc = tocs.get(position);
        int level = toc.getLevel();

        switch (level) {
            case 1:
                return TOC_LEVEl_ONE;
            case 2:
                return TOC_LEVEl_TWO;
            case 3:
                return TOC_LEVEl_THREE;
        }
        return TOC_LEVEl_TWO;
    }


    @Override
    public int getItemCount() {
        return tocs.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TOC_LEVEl_ONE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.toc_item_level_one, parent, false);
                return new ViewHolderOne(view);
            case TOC_LEVEl_TWO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.toc_item_level_two, parent, false);
                return new ViewHolderTwo(view);
            case TOC_LEVEl_THREE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.toc_item_level_three, parent, false);
                return new ViewHolderThree(view);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if( holder instanceof ViewHolderOne){
            ((ViewHolderOne)holder).tv_name.setText(MDetect.getDeviceEncodedText(tocs.get(position).getName()));
        } else if( holder instanceof ViewHolderThree) {
            ((ViewHolderThree) holder).tv_name.setText(MDetect.getDeviceEncodedText(tocs.get(position).getName()));
        } else {
            ((ViewHolderTwo) holder).tv_name.setText(MDetect.getDeviceEncodedText(tocs.get(position).getName()));
        }

    }

    class ViewHolderOne extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(tocs.get(getAdapterPosition()));
        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(tocs.get(getAdapterPosition()));
        }
    }

    class ViewHolderThree extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;

        public ViewHolderThree(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(tocs.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Toc toc);
    }
}
