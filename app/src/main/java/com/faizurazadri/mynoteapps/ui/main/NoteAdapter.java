package com.faizurazadri.mynoteapps.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.faizurazadri.mynoteapps.database.Note;
import com.faizurazadri.mynoteapps.databinding.ItemNoteBinding;
import com.faizurazadri.mynoteapps.helper.NoteDiffCallback;
import com.faizurazadri.mynoteapps.ui.insert.NoteAddUpdateActivity;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final ArrayList<Note> listNotes = new ArrayList<>();
    private final Activity activity;

    NoteAdapter(Activity activity){
        this.activity = activity;
    }

    void setListNotes(List<Note> listNotes){
        final NoteDiffCallback diffCallback = new NoteDiffCallback(this.listNotes, listNotes);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.listNotes.clear();
        this.listNotes.addAll(listNotes);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding itemNoteBinding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(itemNoteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(listNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        final ItemNoteBinding binding;

        NoteViewHolder(ItemNoteBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Note note){
            binding.tvItemTitle.setText(listNotes.get(getBindingAdapterPosition()).getTitle());
            binding.tvItemDate.setText(listNotes.get(getBindingAdapterPosition()).getDate());
            binding.tvItemDescription.setText(listNotes.get(getBindingAdapterPosition()).getDecription());

            binding.cvItemNote.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), NoteAddUpdateActivity.class);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, getBindingAdapterPosition());
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note);
                activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);
            });
        }
    }
}
