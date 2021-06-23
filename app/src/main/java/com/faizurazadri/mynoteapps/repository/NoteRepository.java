package com.faizurazadri.mynoteapps.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.faizurazadri.mynoteapps.database.Note;
import com.faizurazadri.mynoteapps.database.NoteDao;
import com.faizurazadri.mynoteapps.database.NoteRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDao mNoteDao;
    private ExecutorService executorService;

    public NoteRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
    }

    public LiveData<List<Note>> getAllNotes(){
        return mNoteDao.getAllNotes();
    }

    public void insert(final Note note){
        executorService.execute(() -> mNoteDao.insert(note));
    }

    public void delete(final Note note){
        executorService.execute(() -> mNoteDao.delete(note));
    }

    public void update(final Note note){
        executorService.execute(() -> mNoteDao.update(note));
    }
}
