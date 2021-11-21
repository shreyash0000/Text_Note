package com.task.note.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.task.note.R;
import com.task.note.adapter.model.Notes;
import com.task.note.screen.AddNoteActivity;
import com.task.note.screen.EditNoteActivity;
import com.task.note.utils.LinedEditText;
import com.task.note.utils.Utils;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Holder> {
    private Context context;
    private List<Notes> notesList;

    public NoteAdapter(Context context, List<Notes> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    public void filterList(List<Notes> notesList1) {
        this.notesList = notesList1;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_list,parent,false);
        return new NoteAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvEditing.setText("Created at "+ notesList.get(position).getCreated());
        String message = Utils.getTimeAgo(notesList.get(position).getModify());
        Log.d("date_formation",message);
        holder.date.setText(message);
        holder.title.setText(notesList.get(position).getTitle());

        // on Click for Edit Text
        holder.cvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, EditNoteActivity.class);
                in.putExtra("id", notesList.get(position).getId());
                in.putExtra("title", notesList.get(position).getTitle());
                in.putExtra("description", notesList.get(position).getDescription());
                in.putExtra("created", notesList.get(position).getCreated());
                in.putExtra("modify", message);
                context.startActivity(in);
            }
        });

        // if condition match
        holder.tvDescription.setText(notesList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        Log.d("notesList_size", String.valueOf(notesList.size()));
        return notesList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView tvEditing;
        private TextView tvDescription;
        private TextView title;
        private CardView cvClick;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cvClick = (CardView) itemView.findViewById(R.id.cvClick);
            date = (TextView) itemView.findViewById(R.id.tvDate);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            tvEditing = (TextView) itemView.findViewById(R.id.tvEditing);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }
    }
}