package org.ganymede.automaticbell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ganymede.automaticbell.Model.History;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private ArrayList<History> listHistory;

    public HistoryAdapter(ArrayList<History> listHistory) {
        this.listHistory = listHistory;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history,parent,false);

        HistoryHolder historyHolder = new HistoryHolder(view);
        return historyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        History history = listHistory.get(position);
        holder.mWaktu.setText(history.getTime());
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {
        TextView mWaktu;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            mWaktu = itemView.findViewById(R.id.tv_time);
        }
    }
}
