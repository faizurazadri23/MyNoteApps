package com.faizurazadri.mynoteapps.ui.insert;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.faizurazadri.mynoteapps.database.Note;
import com.faizurazadri.mynoteapps.repository.NoteRepository;

public class NoteAddUpdateViewModel extends ViewModel {

    private NoteRepository mNoteRepository;

    public NoteAddUpdateViewModel(Application application) {
        mNoteRepository = new NoteRepository(application);
    }

    public void insert(Note note){
        mNoteRepository.insert(note);
    }

    public void update(Note note){
        mNoteRepository.update(note);
    }

    public void delete(Note note){
        mNoteRepository.delete(note);
    }
}
