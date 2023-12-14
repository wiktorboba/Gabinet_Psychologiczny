package com.example.gabinet_psychologiczny.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabinet_psychologiczny.Database.Relations.VisitWithAnnotationsAndPatientAndService;
import com.example.gabinet_psychologiczny.Dialogs.AddEditAnnotationDialog;
import com.example.gabinet_psychologiczny.Dialogs.EditVisitDialog;
import com.example.gabinet_psychologiczny.Model.Annotation;
import com.example.gabinet_psychologiczny.Other.AnnotationsRecyclerViewAdapter;
import com.example.gabinet_psychologiczny.Other.CalendarUtils;
import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.AnnotationViewModel;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentVisitDetailsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitDetailsFragment extends Fragment implements EditVisitDialog.EditVisitDialogListener, AddEditAnnotationDialog.AddEditAnnotationDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "visit_id";
    private static final String ARG_PARAM2 ="patient_first_name";
    private static final String ARG_PARAM3 ="patient_last_name";

    // TODO: Rename and change types of parameters
    private int id;
    private FragmentVisitDetailsBinding binding;
    private VisitViewModel visitViewModel;
    private AnnotationViewModel annotationViewModel;
    RecyclerView recyclerView;
    AnnotationsRecyclerViewAdapter adapter;
    Visit visit;
    Service service;
    Patient patient;
    List<Annotation> annotations;
    ActivityResultLauncher<Intent> chooseFileActivityResultLauncher;

    private final String[] mimetypes = {"image/*", "audio/*", "text/plain"};

    String[] visitStatusItems;
    String[] paymentStatusItems;

    ImageView visitStatusIcon;
    ImageView paymentStatusIcon;
    ImageView editAnnotationButton;

    boolean optionsClicked = false;
    FloatingActionButton optionsButton;
    FloatingActionButton addAnnotationButton;
    FloatingActionButton editVisitStatus;

    ConstraintLayout addAnnotationsLayout;
    ConstraintLayout editStatusLayout;

    Animation showButtonAnimation;
    Animation hideButtonAnimation;

    public VisitDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VisitDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitDetailsFragment newInstance(int param1) {
        VisitDetailsFragment fragment = new VisitDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVisitDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton buttonMoreOptions = binding.moreOptions;
        recyclerView = binding.annotationsRecyclerview;
        adapter = new AnnotationsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnItemClickListener(new AnnotationsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Annotation annotation) {
                Uri uri = Uri.parse(annotation.getUri());
                openFileFromUri(uri);
            }

            @Override
            public void onEditClick(Annotation annotation) {
                openAddEditAnnotationDialog(annotation);
            }
        });


        TextView description = binding.description;

        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    visit.setDescription(textView.getText().toString());
                    visitViewModel.update(visit);
                    Toast.makeText(getActivity(), "Opis zmieniono pomyślnie!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        optionsButton = binding.moreOptions;
        addAnnotationButton = binding.addAnnotationButton;
        editVisitStatus = binding.editStatusButton;
        addAnnotationsLayout = binding.addAnnotationFloatingButtonLayout;
        editStatusLayout = binding.statusFloatingButtonLayout;
        showButtonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand_buttons_from_bottom);
        hideButtonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hide_butons_to_bottom);

        visitStatusIcon = binding.visitStatusIcon;
        paymentStatusIcon = binding.paymentStatusIcon;

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsClicked = !optionsClicked;
                setVisibility(optionsClicked);
                setAnimation(optionsClicked);
                addAnnotationButton.setClickable(optionsClicked);
                editVisitStatus.setClickable(optionsClicked);
            }
        });

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
                                Annotation annotation = new Annotation(visit.getId(), mimeType, intType, uri.toString());
                                annotationViewModel.insert(annotation);
                            }
                        }
                    }
                }
        );


        addAnnotationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);

                //openChooseFileActivity();
                openAddEditAnnotationDialog(null);
                optionsClicked = !optionsClicked;
                setVisibility(optionsClicked);
                setAnimation(optionsClicked);
            }
        });

        editVisitStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openEditVisitDialog();

                optionsClicked = !optionsClicked;
                setVisibility(optionsClicked);
                setAnimation(optionsClicked);
            }
        });


        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        visitViewModel.getVisitAndServiceById(id).observe(getViewLifecycleOwner(), new Observer<VisitWithAnnotationsAndPatientAndService>() {
            @Override
            public void onChanged(@Nullable VisitWithAnnotationsAndPatientAndService v) {
                visit = v.visit;
                service = v.service;
                patient = v.patient;
                annotations = v.annotations;
                adapter.setVisitsList(annotations);
                setUpVisitInformation();
                setStatus();
            }
        });

        annotationViewModel = new ViewModelProvider(this).get(AnnotationViewModel.class);

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

    private void openChooseFileActivity() {
        Intent fileintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileintent.addCategory(Intent.CATEGORY_OPENABLE);
        fileintent.setType("*/*");
        fileintent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        chooseFileActivityResultLauncher.launch(fileintent);
    }

    private void openFileFromUri(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        Intent intent = new Intent();
        getContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, contentResolver.getType(uri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivity(intent);

    }

    private void setUpVisitInformation() {
        binding.service.setText(service.getName());
        binding.patientName.setText(patient.getFirstName() + "\n" + patient.getLastName());
        binding.visitDate.setText(CalendarUtils.formattedDate(visit.getDay()));
        binding.visitStartEndTime.setText(visit.getStartTime() + " - " + visit.getEndTime());
        binding.description.setText(visit.getDescription());

    }

    private void setStatus(){
        visitStatusItems = getResources().getStringArray(R.array.visit_status_items);
        paymentStatusItems = getResources().getStringArray(R.array.payment_status_items);

        binding.visitStatusText.setText(visitStatusItems[visit.getVisitStatus()]);
        binding.paymentStatusText.setText(paymentStatusItems[visit.getPaymentStatus()]);
        setStatusIcons();
    }

    private void setStatusIcons(){
        int visitStatus = visit.getVisitStatus();
        int paymentStatus = visit.getPaymentStatus();
        switch (visitStatus){
            case 0:
                visitStatusIcon.setImageResource(R.drawable.baseline_pending_actions_24);
                visitStatusIcon.setColorFilter(getResources().getColor(R.color.yellow, null));
                break;
            case 1:
                visitStatusIcon.setImageResource(R.drawable.baseline_content_paste_off_24);
                visitStatusIcon.setColorFilter(getResources().getColor(R.color.red, null));
                break;
            case 2:
            case 3:
                visitStatusIcon.setImageResource(R.drawable.baseline_content_paste_off_24);
                visitStatusIcon.setColorFilter(getResources().getColor(R.color.gray, null));
                break;
            case 4:
                visitStatusIcon.setImageResource(R.drawable.outline_assignment_turned_in_24);
                visitStatusIcon.setColorFilter(getResources().getColor(R.color.green, null));
                break;
        }

        if(paymentStatus==0){
            paymentStatusIcon.setImageResource(R.drawable.baseline_money_off_24);
            paymentStatusIcon.setColorFilter(getResources().getColor(R.color.red, null));
        }
        else {
            paymentStatusIcon.setImageResource(R.drawable.baseline_attach_money_24);
            paymentStatusIcon.setColorFilter(getResources().getColor(R.color.green, null));
        }
    }

    private void setVisibility(boolean clicked){
        if(!clicked){
            addAnnotationsLayout.setVisibility(View.INVISIBLE);
            editStatusLayout.setVisibility(View.INVISIBLE);
        }
        else {
            addAnnotationsLayout.setVisibility(View.VISIBLE);
            editStatusLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setAnimation(boolean clicked){
        if(!clicked){
            addAnnotationsLayout.startAnimation(hideButtonAnimation);
           editStatusLayout.startAnimation(hideButtonAnimation);
        }
        else {
            addAnnotationsLayout.startAnimation(showButtonAnimation);
            editStatusLayout.startAnimation(showButtonAnimation);
        }
    }


    private void openEditVisitDialog(){
        EditVisitDialog editVisitDialog = new EditVisitDialog();

        Bundle args = new Bundle(); // min max times
        args.putInt("visitStatus", visit.getVisitStatus());
        args.putInt("paymentStatus", visit.getPaymentStatus());
        editVisitDialog.setArguments(args);

        editVisitDialog.setTargetFragment(VisitDetailsFragment.this, 1);
        editVisitDialog.show(getFragmentManager(), "Wybierz pacjenta");
    }

    private void openAddEditAnnotationDialog(Annotation annotation){
        AddEditAnnotationDialog addEditAnnotationDialog = new AddEditAnnotationDialog();

        if(annotation != null){
            Bundle args = new Bundle();
            args.putString("annotationName", annotation.getName());
            args.putString("annotationUri", annotation.getUri());
            args.putInt("annotationPosition", annotations.indexOf(annotation));
            addEditAnnotationDialog.setArguments(args);
        }

        addEditAnnotationDialog.setTargetFragment(VisitDetailsFragment.this, 1);
        addEditAnnotationDialog.show(getFragmentManager(), "Edytuj adnotację");
    }

    @Override //editVisitListener
    public void onDialogSuccess(int newVisitStatus, int newPaymentStatus) {
        visit.setVisitStatus(newVisitStatus);
        visit.setPaymentStatus(newPaymentStatus);
        visitViewModel.update(visit);
    }

    @Override //addEditAnnotationListener
    public void onDialogSuccess(String name, String uri) {
        String mimeType = getContext().getContentResolver().getType(Uri.parse(uri));
        int intType = getTypeFromMime(mimeType);
        Annotation annotation = new Annotation(visit.getId(), name, intType, uri.toString());
        annotationViewModel.insert(annotation);
    }

    @Override //addEditAnnotationListener
    public void onDialogSuccess(String name, String uri, int position) {
        String mimeType = getContext().getContentResolver().getType(Uri.parse(uri));
        int intType = getTypeFromMime(mimeType);
        annotations.get(position).setName(name);
        annotations.get(position).setUri(uri);
        annotations.get(position).setType(intType);
        adapter.notifyDataSetChanged();
    }
}