package com.example.gabinet_psychologiczny.Fragments;

import static com.kizitonwose.calendar.core.ExtensionsKt.daysOfWeek;
import static com.kizitonwose.calendar.core.ExtensionsKt.firstDayOfWeekFromLocale;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gabinet_psychologiczny.R;
import com.example.gabinet_psychologiczny.databinding.CalendarDayTitlesContainerBinding;
import com.example.gabinet_psychologiczny.databinding.FragmentBillsBinding;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MarginValues;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBillsBinding binding;
    private CalendarView calendarView;
    private ViewGroup titlesContainer;
    private TextView monthYearText;

    public BillsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillsFragment newInstance(String param1, String param2) {
        BillsFragment fragment = new BillsFragment();
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
        binding = FragmentBillsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = binding.monthCalendarView;
        titlesContainer = binding.titlesContainer;

        monthYearText = binding.monthYearText;

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
                if(calendarDay.getPosition() == DayPosition.MonthDate){

                    LocalDate today = LocalDate.now();
                    if(calendarDay.getDate().equals(today)){
                        container.calendarDayText.setTextColor(getResources().getColor(R.color.teal_700));
                        container.calendarDayText.setTypeface(container.calendarDayText.getTypeface(), Typeface.BOLD);
                    }
                    else
                        container.calendarDayText.setTextColor(getResources().getColor(R.color.dark_gray));
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


        calendarView.setMonthScrollListener(new Function1<CalendarMonth, Unit>() {
            @Override
            public Unit invoke(CalendarMonth calendarMonth) {
                updateTitle();
                return null;
            }
        });
        calendarView.setup(startMonth, endMonth, daysOfWeek.get(0));
        calendarView.scrollToMonth(currentMonth);


    }

    private void updateTitle() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL u")
                .withLocale(new Locale("pl"));

        String title = calendarView.findFirstVisibleMonth().getYearMonth().format(formatter);
        monthYearText.setText(title);
    }



    class DayViewContainer extends ViewContainer {
        public final TextView calendarDayText;

        public DayViewContainer(View view) {
            super(view);
            calendarDayText = view.findViewById(R.id.calendarDayText);
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