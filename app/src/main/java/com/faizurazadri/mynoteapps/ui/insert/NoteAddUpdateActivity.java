package com.faizurazadri.mynoteapps.ui.insert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.faizurazadri.mynoteapps.R;
import com.faizurazadri.mynoteapps.database.Note;
import com.faizurazadri.mynoteapps.databinding.ActivityNoteAddUpdateBinding;
import com.faizurazadri.mynoteapps.helper.DateHelper;
import com.faizurazadri.mynoteapps.viewmodel.ViewModelFactory;

public class NoteAddUpdateActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_positio";

    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    public Note note;
    private int position;

    private NoteAddUpdateViewModel noteAddUpdateViewModel;
    private ActivityNoteAddUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteAddUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteAddUpdateViewModel = obtainViewModel(NoteAddUpdateActivity.this);

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note!=null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit){
            actionBarTitle = getString(R.string.change);
            btnTitle = getString(R.string.update);

            if (note!=null){
                binding.edtTitle.setText(note.getTitle());
                binding.edtDescription.setText(note.getDecription());
            }
        }else {
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
        }

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        binding.btnSubmit.setText(btnTitle);

        binding.btnSubmit.setOnClickListener(v -> {
            String title = binding.edtTitle.getText().toString().trim();
            String description = binding.edtDescription.getText().toString().trim();

            if (title.isEmpty()){
                binding.edtTitle.setError(getString(R.string.empty));
            }else if (description.isEmpty()){
                binding.edtDescription.setError(getString(R.string.empty));
            }else {
                note.setTitle(title);
                note.setDecription(description);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTE, note);
                intent.putExtra(EXTRA_POSITION, position);

                if (isEdit) {
                    noteAddUpdateViewModel.update(note);
                    setResult(RESULT_UPDATE, intent);
                }else {
                    note.setDate(DateHelper.getCurrentDate());
                    noteAddUpdateViewModel.insert(note);
                    setResult(RESULT_ADD, intent);
                }

                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete){
            showAlertDialog(ALERT_DIALOG_DELETE);
        }else if (item.getItemId() == android.R.id.home){
            showAlertDialog(ALERT_DIALOG_CLOSE);
        }else {
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type){
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose){
            dialogTitle =getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        }else {
            dialogMessage = getString(R.string.message_delete);
            dialogTitle = getString(R.string.delete);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    if (!isDialogClose){
                        noteAddUpdateViewModel.delete(note);

                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_POSITION, position);
                        setResult(RESULT_DELETE, intent);
                    }

                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @NonNull
    private static NoteAddUpdateViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return new ViewModelProvider(activity, factory).get(NoteAddUpdateViewModel.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}