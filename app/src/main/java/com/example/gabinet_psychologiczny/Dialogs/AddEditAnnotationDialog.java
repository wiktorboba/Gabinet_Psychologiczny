package com.example.gabinet_psychologiczny.Dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.gabinet_psychologiczny.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class AddEditAnnotationDialog extends DialogFragment {
    private AddEditAnnotationDialog.AddEditAnnotationDialogListener listener;
    private String annotationName = "";
    private String annotationUri = "";
    private int annotationPosition = -1;
    private TextInputEditText editName;
    private TextView pathTextView;

    private ActivityResultLauncher<Intent> chooseFileActivityResultLauncher;
    private final String[] mimetypes = {"image/*", "audio/*", "text/plain"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            annotationName = args.getString("annotationName", "");
            annotationUri = args.getString("annotationUri", "");
            annotationPosition = args.getInt("annotationPosition", -1);
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_edit_annotation_dialog, null);
        builder.setView(view);

        editName = view.findViewById(R.id.editNameInput);
        pathTextView = view.findViewById(R.id.selectedFilePath);


        chooseFileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes

                            Uri uri = result.getData().getData();
                            String mimeType = getContext().getContentResolver().getType(uri);
                            int intType = getTypeFromMime(mimeType);
                            if(intType == -1)
                                Toast.makeText(getActivity(), "Niepoprawny typ pliku!", Toast.LENGTH_SHORT).show();
                            else {
                                annotationUri = uri.toString();
                                setFilePath();
                            }
                        }
                    }
                }
        );

        setAnnotationName();
        setFilePath();


        Button selectFileButton = view.findViewById(R.id.selectFileButton);
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);

                openChooseFileActivity();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelAddEditAnnotationButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button nextButton = view.findViewById(R.id.saveAddEditAnnotationButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                annotationName = editName.getText().toString();
                if(annotationName.isEmpty()){
                    Toast.makeText(getActivity(), "Brak nazwy adnotacji", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(annotationPosition == -1)
                        listener.onDialogSuccess(annotationName, annotationUri);
                    else
                        listener.onDialogSuccess(annotationName, annotationUri, annotationPosition);
                    dismiss();
                }

            }
        });

        Button deleteButton = view.findViewById(R.id.deleteAnnotationButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        if(annotationPosition == -1){
            deleteButton.setVisibility(View.GONE);
            nextButton.setText("DODAJ");
        }
        else {
            selectFileButton.setText("ZMIEŃ PLIK");
        }


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private void setFilePath() {
        String path = annotationUri;
        if(!annotationUri.isEmpty()){
            path = getFileName(Uri.parse(annotationUri));
        }

        pathTextView.setText(path);
    }
    private void setAnnotationName() {
        editName.setText(annotationName);
    }

    private void openChooseFileActivity() {
        Intent fileintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileintent.addCategory(Intent.CATEGORY_OPENABLE);
        fileintent.setType("*/*");
        fileintent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        chooseFileActivityResultLauncher.launch(fileintent);
    }

    private int getTypeFromMime(String mimeType){
        int type = -1;
        if(mimeType.startsWith("image"))
            type = 0;
        else if(mimeType.startsWith("audio"))
            type = 1;
        else if(mimeType.equals("text/plain"))
            type = 2;

        return type;
    }

    private String getFileName(Uri uri) {
        ContentResolver resolver = getContext().getContentResolver();
        resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddEditAnnotationDialog.AddEditAnnotationDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddEditAnnotationDialogListener");
        }
    }
    public interface AddEditAnnotationDialogListener{
        void onDialogSuccess(String name, String uri);
        void onDialogSuccess(String name, String uri, int position);
    }
}
