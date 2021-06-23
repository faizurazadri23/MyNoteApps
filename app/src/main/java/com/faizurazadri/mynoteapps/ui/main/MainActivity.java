package com.faizurazadri.mynoteapps.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.faizurazadri.mynoteapps.R;
import com.faizurazadri.mynoteapps.database.Note;
import com.faizurazadri.mynoteapps.databinding.ActivityMainBinding;
import com.faizurazadri.mynoteapps.ui.insert.NoteAddUpdateActivity;
import com.faizurazadri.mynoteapps.viewmodel.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.faizurazadri.mynoteapps.ui.insert.NoteAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel mainViewModel = obtainViewModel(MainActivity.this);
        mainViewModel.getAllNotes().observe(this, noteObserver);

        adapter = new NoteAdapter(MainActivity.this);

        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotes.setHasFixedSize(true);
        binding.rvNotes.setAdapter(adapter);

        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null){
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD){
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD){
                    showSnackbarMessage(getString(R.string.added));
                }
            }else if (requestCode == REQUEST_UPDATE){
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE){
                    showSnackbarMessage(getString(R.string.changed));
                }else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE){
                    showSnackbarMessage(getString(R.string.deleted));
                }
            }
        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @NonNull
    private static MainViewModel obtainViewModel(AppCompatActivity activity){
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    private final Observer<List<Note>> noteObserver = new Observer<List<Note>>() {
        @Override
        public void onChanged(List<Note> notes) {
            if (notes!=null){
                adapter.setListNotes(notes);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}