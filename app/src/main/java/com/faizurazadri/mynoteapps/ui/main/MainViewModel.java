package com.faizurazadri.mynoteapps.ui.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.faizurazadri.mynoteapps.database.Note;
import com.faizurazadri.mynoteapps.repository.NoteRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private NoteRepository mNoteRepository;

    public MainViewModel(Application application){
        mNoteRepository = new NoteRepository(application);
    }

    LiveData<List<Note>> getAllNotes(){
        return mNoteRepository.getAllNotes();
    }
}
