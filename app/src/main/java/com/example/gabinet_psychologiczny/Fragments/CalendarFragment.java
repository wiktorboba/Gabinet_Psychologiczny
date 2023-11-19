package com.example.gabinet_psychologiczny.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabinet_psychologiczny.Classes.CalendarUtils;
import com.example.gabinet_psychologiczny.Database.Relations.VisitWithPatientAndService;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentCalendarBinding;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCalendarBinding binding;
    private VisitViewModel visitViewModel;
    private LocalDate currentDate;
    private TextView selectedWeek;
    private TextView mondayTextView;
    private TextView tuesdayTextView;
    private TextView wednesdayTextView;
    private TextView thursdayTextView;
    private TextView fridayTextView;
    private ImageView previousWeekButton;
    private ImageView nextWeekButton;
    private int one_hour_height;
    private int top_offset;
    private int dividers_size;

    private ArrayList<ConstraintLayout> daysLayouts;

    private ArrayList<TextView> dayNamesList;

    private ArrayList<CardView> displayedVisits = new ArrayList<>();
    private ArrayList<CardView> displayedFreeTimes = new ArrayList<>();

    private LiveData<List<VisitWithPatientAndService>> observedWeek;
    private final int breakTime = 5;
    private final int minFreeTime = 30;


    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpWidgets();

        visitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);

        one_hour_height = getResources().getDimensionPixelSize(R.dimen.visit_hour_height);
        top_offset = getResources().getDimensionPixelSize(R.dimen.new_top_offset);
        dividers_size = getResources().getDimensionPixelSize(R.dimen.dividers_size);


        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousWeek();
            }
        });

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextWeek();
            }
        });

        if(currentDate == null)
            currentDate = LocalDate.now();
    }

    private void setUpWidgets(){
        selectedWeek = binding.selectedWeek;
        previousWeekButton = binding.previousWeekButton;
        nextWeekButton = binding.nextWeekButton;
        mondayTextView = binding.mondayTextView;
        tuesdayTextView = binding.tuesdayTextView;
        wednesdayTextView = binding.wednesdayTextView;
        thursdayTextView = binding.thursdayTextView;
        fridayTextView = binding.fridayTextView;

        dayNamesList = new ArrayList<>();
        dayNamesList.add(mondayTextView);
        dayNamesList.add(tuesdayTextView);
        dayNamesList.add(wednesdayTextView);
        dayNamesList.add(thursdayTextView);
        dayNamesList.add(fridayTextView);

        ConstraintLayout mondayLayout = binding.mondayLayout;
        ConstraintLayout tuesdayLayout = binding.tuesdayLayout;
        ConstraintLayout wednesdayLayout = binding.wednesdayLayout;
        ConstraintLayout thursdayLayout = binding.thursdayLayout;
        ConstraintLayout fridayLayout = binding.fridayLayout;

        daysLayouts = new ArrayList<>();
        daysLayouts.add(mondayLayout);
        daysLayouts.add(tuesdayLayout);
        daysLayouts.add(wednesdayLayout);
        daysLayouts.add(thursdayLayout);
        daysLayouts.add(fridayLayout);
    }

    private void setWeekView(){
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(currentDate);
        String week = CalendarUtils.formattedDate(days.get(0)) + " - " + CalendarUtils.formattedDate(days.get(days.size()-1));
        selectedWeek.setText(week);
        setDayNames(days);

        observedWeek = visitViewModel.getVisitAndServiceFromDayToDay(days.get(0), days.get(days.size()-1));
        observedWeek.observe(getViewLifecycleOwner(), new Observer<List<VisitWithPatientAndService>>() {
            @Override
            public void onChanged(@Nullable List<VisitWithPatientAndService> visitWithPatientAndServices) {
                ArrayList<ArrayList<VisitWithPatientAndService>> visitsPerDay = getVisitsPerDay(days, visitWithPatientAndServices);
                setUpVisitsInCalendar(visitsPerDay);
                observedWeek.removeObserver(this);
            }
        });

    }

    private void setUpVisitsInCalendar(ArrayList<ArrayList<VisitWithPatientAndService>> visitsPerDay) {
        int i=0;
        if(!displayedVisits.isEmpty() || !displayedFreeTimes.isEmpty()){
            removeFromView(displayedVisits);
            displayedVisits = new ArrayList<>();

            removeFromView(displayedFreeTimes);
            displayedFreeTimes = new ArrayList<>();
        }


        for(ConstraintLayout dayLayout: daysLayouts){
            ArrayList<VisitWithPatientAndService> visits = visitsPerDay.get(i);

            LocalTime previousVisitEndTime = LocalTime.of(6, 0).minusMinutes(5);
            LocalTime freeTimeStart;
            LocalTime freeTimeEnd;
            int freeTimeDuration = 0;
            for(VisitWithPatientAndService visit: visits){
                CardView visitCardView = createCardViewForVisit(dayLayout, visit);
                displayedVisits.add(visitCardView);
                visitCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("visit_id", visit.visit.getId());
                        Fragment fragment = new VisitDetailsFragment();
                        fragment.setArguments(bundle);
                        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
                        replaceFragment(fragment);
                    }
                });

                freeTimeStart = previousVisitEndTime.plusMinutes(breakTime);
                freeTimeEnd = visit.visit.getStartTime().minusMinutes(breakTime);

                freeTimeDuration = (int)Duration.between(freeTimeStart, freeTimeEnd).toMinutes();

                if(freeTimeDuration >= minFreeTime){
                    CardView freeTimeCardView = createCardViewForFreeTime(dayLayout, freeTimeStart, freeTimeEnd);
                    displayedFreeTimes.add(freeTimeCardView);
                    freeTimeCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // open addVisitDialog with selected date
                            // let user choose patient, time(TimesPickerDialog) and service
                        }
                    });
                }
                previousVisitEndTime = visit.visit.getEndTime();
            }

            // last freetime in a day
            freeTimeStart = previousVisitEndTime.plusMinutes(5);
            freeTimeEnd = LocalTime.of(22, 0);
            freeTimeDuration = (int)Duration.between(freeTimeStart, freeTimeEnd).toMinutes();
            if(freeTimeDuration >= minFreeTime){
                CardView freeTimeCardView = createCardViewForFreeTime(dayLayout, freeTimeStart, freeTimeEnd);
                displayedFreeTimes.add(freeTimeCardView);
                freeTimeCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO
                    }
                });
            }

            i++;
        }
    }

    private CardView createCardViewForVisit(ConstraintLayout parentLayout, VisitWithPatientAndService visit){
        //calculate cardview height and offset
        double visitDuration = Duration.between(visit.visit.getStartTime(), visit.visit.getEndTime()).toMinutes() / 60f;
        double verticalOffset = Duration.between(LocalTime.of(6, 0), visit.visit.getStartTime()).toMinutes() / 60f;

        //inflate cardview with layout from xml
        CardView visitCardView = (CardView)getLayoutInflater().inflate(R.layout.calendar_visit_item, parentLayout, false);
        visitCardView.setId(View.generateViewId());

        //set up cardview height
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)visitCardView.getLayoutParams();
        layoutParams.height = (int)(visitDuration*one_hour_height) + ((int)Math.ceil(visitDuration)-1)*dividers_size;
        visitCardView.setLayoutParams(layoutParams);
        parentLayout.addView(visitCardView);

        //set up cardview offset (horizontal placement)
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(parentLayout);
        constraintSet.connect(visitCardView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top_offset + (int)(verticalOffset*one_hour_height) + ((int)Math.floor(verticalOffset))*dividers_size);
        constraintSet.applyTo(parentLayout);

        //set up information inside cardview
        TextView time = visitCardView.findViewById(R.id.visitStartEndTime);
        TextView patientName = visitCardView.findViewById(R.id.patientName);
        time.setText(CalendarUtils.formattedTime(visit.visit.getStartTime()) + " - " + CalendarUtils.formattedTime(visit.visit.getEndTime()));
        patientName.setText(visit.patient.getFirstName() + " " + visit.patient.getLastName());

        return visitCardView;
    }

    private CardView createCardViewForFreeTime(ConstraintLayout parentLayout, LocalTime startTime, LocalTime endTime){
        //calculate cardview height and offset
        double visitDuration = Duration.between(startTime, endTime).toMinutes() / 60f;
        double verticalOffset = Duration.between(LocalTime.of(6, 0), startTime).toMinutes() / 60f;

        //inflate cardview with layout from xml
        CardView freetimeCardView = (CardView)getLayoutInflater().inflate(R.layout.calendar_freetime_item, parentLayout, false);
        freetimeCardView.setId(View.generateViewId());

        //set up cardview height
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)freetimeCardView.getLayoutParams();
        layoutParams.height = (int)(visitDuration*one_hour_height) + ((int)Math.ceil(visitDuration)-1)*dividers_size;
        freetimeCardView.setLayoutParams(layoutParams);
        parentLayout.addView(freetimeCardView);

        //set up cardview offset (horizontal placement)
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(parentLayout);
        constraintSet.connect(freetimeCardView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top_offset + (int)(verticalOffset*one_hour_height) + ((int)Math.floor(verticalOffset))*dividers_size);
        constraintSet.applyTo(parentLayout);

        return freetimeCardView;
    }

    private void removeFromView(ArrayList<CardView> cardViews){
        for(CardView cardView: cardViews){
            ConstraintLayout parent = (ConstraintLayout)cardView.getParent();
            parent.removeView(cardView);
        }
    }

    private void previousWeek(){
        currentDate = currentDate.minusWeeks(1);
        setWeekView();
    }

    private void nextWeek(){
        currentDate = currentDate.plusWeeks(1);
        setWeekView();
    }

    private void setDayNames(ArrayList<LocalDate> days){
        int i=0;
        for(TextView name: dayNamesList){
            name.setText(CalendarUtils.dayMonthFromDate(days.get(i)));
            i++;
        }
    }

    private ArrayList<ArrayList<VisitWithPatientAndService>> getVisitsPerDay(ArrayList<LocalDate> days, List<VisitWithPatientAndService> allVisitsInWeek){
        ArrayList<ArrayList<VisitWithPatientAndService>> visitsPerDay = new ArrayList<>();
        for(LocalDate day: days){
            ArrayList<VisitWithPatientAndService> visitsThisDay = new ArrayList<>();
            for(VisitWithPatientAndService visit: allVisitsInWeek){
                if(visit.visit.getDay().equals(day)){
                    visitsThisDay.add(visit);
                    //allVisitsInWeek.remove(visit); //TODO
                }
            }
            visitsThisDay.sort(new Comparator<VisitWithPatientAndService>() {
                @Override
                public int compare(VisitWithPatientAndService v1, VisitWithPatientAndService v2) {
                    return v1.visit.getStartTime().compareTo(v2.visit.getStartTime());
                }
            });
            visitsPerDay.add(visitsThisDay);
        }
        return visitsPerDay;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addFragmentFromBottom(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top);
        fragmentTransaction.replace(R.id.bottom_container, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
        setWeekView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFromView(displayedVisits);
        displayedVisits = new ArrayList<>();

        removeFromView(displayedFreeTimes);
        displayedFreeTimes = new ArrayList<>();
    }
}