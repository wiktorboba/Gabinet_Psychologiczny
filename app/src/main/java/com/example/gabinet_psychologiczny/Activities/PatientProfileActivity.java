package com.example.gabinet_psychologiczny.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.gabinet_psychologiczny.Fragments.PatientDetailsFragment;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.databinding.ActivityPatientProfileBinding;

public class PatientProfileActivity extends AppCompatActivity {
    private ActivityPatientProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        int id = i.getIntExtra("id", 0);

        Bundle bundle = new Bundle();
        bundle.putInt("patient_id", id);
        Fragment fragment = new PatientDetailsFragment();
        fragment.setArguments(bundle);

        replaceFragment(fragment);

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout2, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}