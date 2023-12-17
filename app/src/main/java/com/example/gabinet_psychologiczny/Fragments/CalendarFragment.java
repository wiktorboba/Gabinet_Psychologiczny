package com.example.gabinet_psychologiczny.Fragments;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;
import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabinet_psychologiczny.Database.Relations.VisitWithAnnotationsAndPatientAndService;
import com.example.gabinet_psychologiczny.Dialogs.AddVisitDialog;
import com.example.gabinet_psychologiczny.Other.CalendarUtils;
import com.example.gabinet_psychologiczny.Dialogs.TimesPickerDialog;
import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.ViewModel.VisitViewModel;
import com.example.gabinet_psychologiczny.databinding.FragmentCalendarBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements TimesPickerDialog.TimesPickerDialogListener, AddVisitDialog.AddVisitDialogListener{

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
    private YearMonth currentMonth;
    ArrayList<LocalDate> daysInThisWeek;
    ArrayList<LocalDate> daysInThisMonth;
    ArrayList<Boolean> hasVisitsPerDay;
    Boolean indicatorsReady = false;
    private TextView selectedDate;
    private TextView mondayTextView;
    private TextView tuesdayTextView;
    private TextView wednesdayTextView;
    private TextView thursdayTextView;
    private TextView fridayTextView;
    private ImageView previousWeekButton;
    private ImageView nextWeekButton;
    private ImageView addVisitStateButton;
    private int one_hour_height;
    private int top_offset;
    private int dividers_size;
    private int visit_indicator_size;

    private ArrayList<ConstraintLayout> daysLayouts;

    private ArrayList<TextView> dayNamesList;

    private ArrayList<CardView> displayedVisits = new ArrayList<>();
    private ArrayList<CardView> displayedFreeTimes = new ArrayList<>();

    private LiveData<List<VisitWithAnnotationsAndPatientAndService>> observedWeek;
    private LiveData<List<VisitWithAnnotationsAndPatientAndService>> observedMonth;
    private final int breakTime = 5;
    private final int minFreeTime = 45;
    private boolean addVisitState = false;

    private boolean isMonthlyLayout = false; // false - weekly, true - monthly
    private CalendarView calendarView;
    private ViewGroup titlesContainer;
    private Boolean calledFromAddVisitDialog = false;
    private View selectedDayForList;
    private TextView textViewOfSelectedDay;
    private Boolean isDaySelected = false;
    private Button generateListButton;

    private Animation showButtonAnimation;

    private Animation hideButtonAnimation;

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

        if(currentDate == null){
            currentDate = LocalDate.now();
            currentMonth = YearMonth.from(currentDate);
        }
        //setMonthView();

        one_hour_height = getResources().getDimensionPixelSize(R.dimen.visit_hour_height);
        top_offset = getResources().getDimensionPixelSize(R.dimen.new_top_offset);
        dividers_size = getResources().getDimensionPixelSize(R.dimen.dividers_size);
        visit_indicator_size = getResources().getDimensionPixelSize(R.dimen.visit_indicator_size);

        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isMonthlyLayout){
                    changeToMonthlyView();
                }
            }
        });

        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isMonthlyLayout){
                    previousMonth();
                }
                else {
                    previousWeek();
                }
            }
        });

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isMonthlyLayout){
                    nextMonth();
                }
                else {
                    nextWeek();
                }
            }
        });

        addVisitStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddVisitState(!addVisitState);
            }
        });

        setUpMonthlyCalendar();

    }

    private void changeToMonthlyView(){
        isMonthlyLayout = true;
        setMonthView();
        //calendarView.scrollToMonth(currentMonth);
        addVisitStateButton.setVisibility(View.INVISIBLE);
        binding.weeklyCalendarView.setVisibility(View.INVISIBLE);
        binding.bottomContainer.setVisibility(View.VISIBLE);
    }

    private void changeToWeeklyView(){
        isMonthlyLayout = false;
        setWeekView();
        if(!calledFromAddVisitDialog)
            addVisitStateButton.setVisibility(View.VISIBLE);
        binding.weeklyCalendarView.setVisibility(View.VISIBLE);
        binding.bottomContainer.setVisibility(View.INVISIBLE);
    }

    private void setUpWidgets(){
        selectedDate = binding.selectedWeek;
        previousWeekButton = binding.previousWeekButton;
        nextWeekButton = binding.nextWeekButton;
        addVisitStateButton = binding.addVisitStateButton;
        mondayTextView = binding.mondayTextView;
        tuesdayTextView = binding.tuesdayTextView;
        wednesdayTextView = binding.wednesdayTextView;
        thursdayTextView = binding.thursdayTextView;
        fridayTextView = binding.fridayTextView;

        calendarView = binding.monthCalendarView;
        titlesContainer = binding.titlesContainer;
        generateListButton = binding.generateListButton;
        showButtonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.expand_buttons_from_bottom);
        hideButtonAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.hide_butons_to_bottom);

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

    private void setUpMonthlyCalendar(){
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @NonNull
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, CalendarMonth calendarMonth) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL u")
                        .withLocale(new Locale("pl"));

                String title = calendarMonth.getYearMonth().format(formatter);
                //container.calendarMonthText.setText(title);
                //monthYearText.setText(title);
            }
        });

        /*        calendarView.setMonthFooterBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @NonNull
            @Override
            public MonthViewContainer create(@NonNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthViewContainer container, CalendarMonth calendarMonth) {

            }
        });
       */

        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }


            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay calendarDay) {
                container.calendarDayText.setText(Integer.toString(calendarDay.getDate().getDayOfMonth()));
                container.calendarDay = calendarDay;
                container.calendarDayText.setTypeface(Typeface.DEFAULT);
                if(calendarDay.getPosition() == DayPosition.MonthDate){
                    LocalDate today = LocalDate.now();
                    if(calendarDay.getDate().equals(today)){
                        container.calendarDayText.setTextColor(getResources().getColor(R.color.teal_700));
                        container.calendarDayText.setTypeface(container.calendarDayText.getTypeface(), Typeface.BOLD);
                    }
                    else{
                        container.calendarDayText.setTextColor(getResources().getColor(R.color.dark_gray));
                    }

                    container.visitIndicatorDot.setVisibility(View.INVISIBLE);
                    if(hasVisitsPerDay != null && indicatorsReady && YearMonth.from(calendarDay.getDate()).equals(currentMonth)){
                        int dayIndex = calendarDay.getDate().getDayOfMonth()-1;
                        if(hasVisitsPerDay.get(dayIndex)){
                            //ImageView visitIndicator = new ImageView(container.visitIndicatorsLayout.getContext());
                            //visitIndicator.setId(View.generateViewId());

                            //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(visit_indicator_size, visit_indicator_size);
                            //visitIndicator.setLayoutParams(layoutParams);
                            //container.visitIndicatorsLayout.addView(visitIndicator);
                            container.visitIndicatorDot.setVisibility(View.VISIBLE);
                        }

                    }

                }

                else
                    container.calendarDayText.setTextColor(getResources().getColor(R.color.light_gray));
            }
        });

        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(100);  // Adjust as needed
        YearMonth endMonth = currentMonth.plusMonths(100);  // Adjust as needed
        DayOfWeek firstDayOfWeek = firstDayOfWeekFromLocale(); // Available from the library
        List<DayOfWeek> daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY);

        int count = titlesContainer.getChildCount();
        for(int i=0; i<count; i++){
            TextView textView = (TextView) titlesContainer.getChildAt(i);
            DayOfWeek dayOfWeek = daysOfWeek.get(i);
            String title = dayOfWeek.getDisplayName(TextStyle.SHORT, new Locale("pl"));
            textView.setText(title);
        }

        calendarView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
            }
        });


        /*
        calendarView.setMonthScrollListener(new Function1<CalendarMonth, Unit>() {
            @Override
            public Unit invoke(CalendarMonth calendarMonth) {
                updateTitle();
                setMonthView();
                indicatorsReady = true;
                return null;
            }
        });

        */
        calendarView.setup(startMonth, endMonth, daysOfWeek.get(0));
        calendarView.scrollToMonth(currentMonth);

    }

    private void setWeekView(){
        daysInThisWeek = CalendarUtils.daysInWeekArray(currentDate);
        currentMonth = YearMonth.from(currentDate);

        String week = CalendarUtils.formattedDateShort(daysInThisWeek.get(0)) + " - " + CalendarUtils.formattedDateShort(daysInThisWeek.get(daysInThisWeek.size()-1));
        selectedDate.setText(week);
        setDayNames(daysInThisWeek);

        observedWeek = visitViewModel.getVisitAndServiceFromDayToDay(daysInThisWeek.get(0), daysInThisWeek.get(daysInThisWeek.size()-1));
        observedWeek.observe(getViewLifecycleOwner(), new Observer<List<VisitWithAnnotationsAndPatientAndService>>() {
            @Override
            public void onChanged(@Nullable List<VisitWithAnnotationsAndPatientAndService> visitWithAnnotationsAndPatientAndServices) {
                ArrayList<ArrayList<VisitWithAnnotationsAndPatientAndService>> visitsPerDay = getVisitsPerDay(daysInThisWeek, visitWithAnnotationsAndPatientAndServices);
                setUpVisitsInCalendar(visitsPerDay);
                observedWeek.removeObserver(this);
            }
        });

    }

    private void setMonthView(){
        indicatorsReady = false;
        daysInThisMonth = CalendarUtils.daysInMonthArray(currentMonth);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL u")
                .withLocale(new Locale("pl"));

        String month = currentMonth.format(formatter);
        selectedDate.setText(month);


        observedMonth = visitViewModel.getVisitAndServiceFromDayToDay(currentMonth.atDay(1), currentMonth.atEndOfMonth());
        observedMonth.observe(getViewLifecycleOwner(), new Observer<List<VisitWithAnnotationsAndPatientAndService>>() {
            @Override
            public void onChanged(@Nullable List<VisitWithAnnotationsAndPatientAndService> visitWithAnnotationsAndPatientAndServices) {
                hasVisitsPerDay = checkVisitsPerDay(daysInThisMonth, visitWithAnnotationsAndPatientAndServices);
                observedMonth.removeObserver(this);
                indicatorsReady = true;
                calendarView.notifyMonthChanged(currentMonth);
                calendarView.smoothScrollToMonth(currentMonth);
            }
        });
    }

    private void setUpVisitsInCalendar(ArrayList<ArrayList<VisitWithAnnotationsAndPatientAndService>> visitsPerDay) {
        int i=0;
        if(!displayedVisits.isEmpty() || !displayedFreeTimes.isEmpty()){
            removeFromView(displayedVisits);
            displayedVisits = new ArrayList<>();

            removeFromView(displayedFreeTimes);
            displayedFreeTimes = new ArrayList<>();
        }


        for(ConstraintLayout dayLayout: daysLayouts){
            ArrayList<VisitWithAnnotationsAndPatientAndService> visits = visitsPerDay.get(i);

            LocalTime previousVisitEndTime = LocalTime.of(6, 0).minusMinutes(5);
            LocalTime freeTimeStart;
            LocalTime freeTimeEnd;
            int freeTimeDuration = 0;
            for(VisitWithAnnotationsAndPatientAndService visit: visits){
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
                    int startHour = freeTimeStart.getHour();
                    int endHour = freeTimeEnd.getHour();
                    int startMinute = freeTimeStart.getMinute();
                    int endMinute = freeTimeEnd.getMinute();
                    int dayIndex = i;
                    freeTimeCardView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            openTimePickerDialog(startHour, endHour, startMinute, endMinute, dayIndex, minFreeTime);
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
                int startHour = freeTimeStart.getHour();
                int endHour = freeTimeEnd.getHour();
                int startMinute = freeTimeStart.getMinute();
                int endMinute = freeTimeEnd.getMinute();
                int dayIndex = i;
                freeTimeCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openTimePickerDialog(startHour, endHour, startMinute, endMinute, dayIndex, minFreeTime);
                    }
                });
            }

            i++;
        }
        setAddVisitState(addVisitState);
    }

    private CardView createCardViewForVisit(ConstraintLayout parentLayout, VisitWithAnnotationsAndPatientAndService visit){
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

    private void previousMonth(){
        currentMonth = currentMonth.minusMonths(1);
        setMonthView();
    }

    private void nextMonth(){
        currentMonth = currentMonth.plusMonths(1);
        setMonthView();
    }

    private void setDayNames(ArrayList<LocalDate> days){
        int i=0;
        for(TextView name: dayNamesList){
            name.setText(CalendarUtils.dayMonthFromDate(days.get(i)));
            if(days.get(i).equals(LocalDate.now())){
                name.setTextColor(getResources().getColor(R.color.teal_700));
                name.setTypeface(name.getTypeface(), Typeface.BOLD);
            }
            i++;
        }
    }

    private ArrayList<ArrayList<VisitWithAnnotationsAndPatientAndService>> getVisitsPerDay(ArrayList<LocalDate> days, List<VisitWithAnnotationsAndPatientAndService> allVisitsInWeek){
        ArrayList<ArrayList<VisitWithAnnotationsAndPatientAndService>> visitsPerDay = new ArrayList<>();
        for(LocalDate day: days){
            ArrayList<VisitWithAnnotationsAndPatientAndService> visitsThisDay = new ArrayList<>();
            for(VisitWithAnnotationsAndPatientAndService visit: allVisitsInWeek){
                if(visit.visit.getDay().equals(day)){
                    visitsThisDay.add(visit);
                    //allVisitsInWeek.remove(visit); //TODO
                }
            }
            visitsThisDay.sort(new Comparator<VisitWithAnnotationsAndPatientAndService>() {
                @Override
                public int compare(VisitWithAnnotationsAndPatientAndService v1, VisitWithAnnotationsAndPatientAndService v2) {
                    return v1.visit.getStartTime().compareTo(v2.visit.getStartTime());
                }
            });
            visitsPerDay.add(visitsThisDay);
        }
        return visitsPerDay;
    }

    private ArrayList<Boolean> checkVisitsPerDay(ArrayList<LocalDate> days, List<VisitWithAnnotationsAndPatientAndService> allVisitsInMonth){
        ArrayList<Boolean> hasAnyVisits = new ArrayList<>();
        for(LocalDate day: days){
            Boolean hasVisitsThisDay = false;
            for(VisitWithAnnotationsAndPatientAndService visit: allVisitsInMonth){
                if(visit.visit.getDay().equals(day)){
                    hasVisitsThisDay = true;
                    break;
                }
            }
            hasAnyVisits.add(hasVisitsThisDay);
        }
        return hasAnyVisits;
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

    private void openAddVisitDialog(int startHour, int startMinutes, int endHour, int endMinutes, int dayIndex){
        LocalDate day = daysInThisWeek.get(dayIndex);
        Bundle bundle = new Bundle();
       // bundle.putInt("patientId", 2);
       // bundle.putString("patientFirstName", "Anna");
       // bundle.putString("patientLastName", "Nowak");
        bundle.putInt("year", day.getYear());
        bundle.putInt("month", day.getMonthValue());
        bundle.putInt("day", day.getDayOfMonth());

        bundle.putInt("startHour", startHour);
        bundle.putInt("startMinute", startMinutes);
        bundle.putInt("endHour", endHour);
        bundle.putInt("endMinute", endMinutes);

        AddVisitDialog addVisitDialog = new AddVisitDialog();
        addVisitDialog.setArguments(bundle);

        addVisitDialog.setTargetFragment(CalendarFragment.this, 1);
        addVisitDialog.show(getFragmentManager(), "Dodaj wizyte");
    }

    private void openTimePickerDialog(int minStartHour, int maxEndHour, int minStartMinutes, int maxEndMinutes, int day, int minTime){

        Bundle bundle = new Bundle();
        bundle.putInt("minHour", minStartHour);
        bundle.putInt("maxHour", maxEndHour);
        bundle.putInt("minMinutes", minStartMinutes);
        bundle.putInt("maxMinutes", maxEndMinutes);
        bundle.putInt("dayIndex", day);
        bundle.putInt("minFreeTime", minTime);

        TimesPickerDialog timesPickerDialog = new TimesPickerDialog();
        timesPickerDialog.setArguments(bundle);

        timesPickerDialog.setTargetFragment(CalendarFragment.this, 1);
        timesPickerDialog.show(getFragmentManager(), "Czas trwania uslugi");
        //getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
        //replaceFragment(fragment);
    }

    private void setAddVisitState(boolean state){
        int freeTimeVisibility;
        float visitsVisibility;
        if(state){
            freeTimeVisibility = View.VISIBLE;
            visitsVisibility = 0.3f;
        }
        else {
            freeTimeVisibility = View.INVISIBLE;
            visitsVisibility = 1f;
        }

        for(CardView freeTime : displayedFreeTimes){
            freeTime.setVisibility(freeTimeVisibility);
        }
        for(CardView visit : displayedVisits){
            visit.setAlpha(visitsVisibility);
            visit.setEnabled(!state);
        }
        addVisitState = state;
    }

    @Override // TimesPickerDialogListener
    public void onDialogSuccess(int startHour, int startMinutes, int endHour, int endMinutes, int dayIndex) {
        if(!calledFromAddVisitDialog)
            openAddVisitDialog(startHour, startMinutes, endHour, endMinutes, dayIndex);
        else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                Bundle bundle = new Bundle();
                LocalDate day = daysInThisWeek.get(dayIndex);

                bundle.putInt("year", day.getYear());
                bundle.putInt("month", day.getMonthValue());
                bundle.putInt("day", day.getDayOfMonth());
                bundle.putInt("startHour", startHour);
                bundle.putInt("startMinute", startMinutes);
                bundle.putInt("endHour", endHour);
                bundle.putInt("endMinute", endMinutes);

                fragmentManager.setFragmentResult("selectedDate", bundle);
                fragmentManager.popBackStack();
            }
        }
    }

    @Override // AddVisitDialogListener
    public void onDialogSuccess() {
        setWeekView();
        setAddVisitState(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        if(bottomNavigationView != null)
            bottomNavigationView.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if(count >=1){
            String name = fragmentManager.getBackStackEntryAt(count-1).getName();
            if(name == "AddVisitDialog"){
                calledFromAddVisitDialog = true;
                setAddVisitState(true);
                addVisitStateButton.setVisibility(View.INVISIBLE);
            }
        }

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




    class DayViewContainer extends ViewContainer {
        public final TextView calendarDayText;
        public CalendarDay calendarDay;
        public final LinearLayout visitIndicatorsLayout;
        public final ImageView visitIndicatorDot;

        public DayViewContainer(View view) {
            super(view);
            calendarDayText = view.findViewById(R.id.calendarDayText);
            visitIndicatorsLayout = view.findViewById(R.id.dayVisitsIndicatorsLayout);
            visitIndicatorDot = view.findViewById(R.id.visitInd);


            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentDate = calendarDay.getDate();
                    changeToWeeklyView();
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(isDaySelected){
                        textViewOfSelectedDay.setTextColor(getResources().getColor(R.color.dark_gray));
                        selectedDayForList.setSelected(false);
                        if(view == selectedDayForList){
                            isDaySelected = false;

                            generateListButton.setVisibility(View.INVISIBLE);
                            generateListButton.startAnimation(hideButtonAnimation);
                        }
                        else {
                            calendarDayText.setTextColor(getResources().getColor(R.color.white));
                            view.setSelected(true);
                            selectedDayForList = view;
                            textViewOfSelectedDay = calendarDayText;
                        }
                    }
                    else{
                        calendarDayText.setTextColor(getResources().getColor(R.color.white));
                        view.setSelected(true);
                        selectedDayForList = view;
                        textViewOfSelectedDay = calendarDayText;
                        isDaySelected = true;

                        generateListButton.setVisibility(View.VISIBLE);
                        generateListButton.startAnimation(showButtonAnimation);

                    }
                    return true;
                }
            });
        }
    }

    class MonthViewContainer extends ViewContainer {
        public final TextView calendarMonthText;


        public MonthViewContainer(View view) {
            super(view);
            calendarMonthText = view.findViewById(R.id.calendarMonthText);
        }
    }

}