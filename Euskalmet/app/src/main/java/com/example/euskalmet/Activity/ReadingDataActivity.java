package com.example.euskalmet.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.euskalmet.R;
import com.example.euskalmet.Room.Entity.Reading;
import com.example.euskalmet.Room.MeteoController;
import com.example.euskalmet.databinding.ActivityMainBinding;
import com.example.euskalmet.ui.main.fragment.DatePickerFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.*;

import static android.app.PendingIntent.getActivity;


public class ReadingDataActivity extends AppCompatActivity {

    private MeteoController meteoController;
    private ActivityMainBinding binding;
    private String stationId;
    private String stationName;
    private String selectedDate;
    private LineChart mChart;
    private Calendar currentDate;
    private FloatingActionButton reloadButton;
    private Boolean chartLoaded = false;
    private List<Reading> dateReadingList;
    private Spinner categorySpinner;
    private EditText readingDateText;
    private TextView averageTextView;
    private TextView titleTextView;
    private LineData data;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        meteoController = MeteoController.getMeteoController(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stationId = extras.getString("stationId");
            stationName = extras.getString("stationName");
        }
        initUi();
        initCalendar();
    }

    private void initUi() {
        setContentView(R.layout.activity_reading);
        averageTextView = findViewById(R.id.averageTextView);
        readingDateText = findViewById(R.id.readingDateText);
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(stationName);
        readingDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        categorySpinner = findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDayReadings(selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] items = new String[]{"temperature","precipitation","mean_speed","humidity"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        categorySpinner.setAdapter(adapter);
    }

    private void showDatePickerDialog() {

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                calendar.setTime(new Date());
                Date currentDate = new Date();

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                Date dateRepresentation = cal.getTime();

                if (currentDate.compareTo(dateRepresentation) >= 0) {
                    calendar.setTime(new Date(year, month, day));
                    selectedDate = year + "/" + (month <= 10 ? "0" + (month + 1) : month + 1) + "/" + (day <= 10 ? "0" + day : day);
                    readingDateText.setText(selectedDate);
                    getDayReadings(selectedDate);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReadingDataActivity.this);
                    builder.setMessage("Fecha inválida: \nescoge una fecha anterior a la actual \n(o la actual)")
                            .setTitle("Atención");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void getDayReadings(String dateString) {
        HandlerThread handlerThread = new HandlerThread("StartDayReadings");
        handlerThread.start();
        ArrayList<Entry> chartValues = new ArrayList<>();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        chartLoaded = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                double averageValue;
                double totalValue = 0;
                dateReadingList = new ArrayList<>();
                mChart = findViewById(R.id.chart);

                mChart.setTouchEnabled(true);
                mChart.setPinchZoom(true);
                List<List> dataLists = meteoController.getDateReadings(dateString, stationId, categorySpinner.getSelectedItem().toString());
                for (int i = 0; i < dataLists.get(0).size(); i++) {
                    Reading newReading = new Reading();
                    newReading.readingType = "temperature";
                    newReading.readingId = "t" + i;
                    newReading.readingData = (double) dataLists.get(1).get(i);
                    newReading.readingDateTime = (String) dataLists.get(0).get(i);
                    newReading.stationId = stationId;
                    dateReadingList.add(newReading);
                    chartValues.add(new Entry(Float.parseFloat(newReading.readingDateTime.substring(0, 2) + '.' + newReading.readingDateTime.substring(3, 5)), (float) newReading.readingData));
                    totalValue += newReading.readingData;
                }
                averageValue = totalValue / chartValues.size();
                averageTextView.setText("Average : " + averageValue);
                LineDataSet set1;
                if (mChart.getData() != null &&
                        mChart.getData().getDataSetCount() > 0) {
                    set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                    set1.setValues(chartValues);
                    set1.setLabel(categorySpinner.getSelectedItem().toString());
                    mChart.setData(data);
                    mChart.getData().notifyDataChanged();
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                    mChart.getDescription().setText(categorySpinner.getSelectedItem().toString() + " along the day");
                } else {
                    set1 = new LineDataSet(chartValues, categorySpinner.getSelectedItem().toString());
                    set1.setDrawIcons(false);
                    set1.enableDashedLine(10f, 5f, 0f);
                    set1.enableDashedHighlightLine(10f, 5f, 0f);
                    set1.setColor(Color.parseColor("#8200C8"));
                    set1.setCircleColor(Color.parseColor("#8200C8"));
                    set1.setLineWidth(1f);
                    set1.setCircleRadius(3f);
                    set1.setDrawCircleHole(false);
                    set1.setValueTextSize(9f);
                    set1.setDrawFilled(true);
                    set1.setFormLineWidth(1f);
                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set1.setFormSize(15.f);
                    set1.setFillColor(Color.DKGRAY);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    data = new LineData(dataSets);
                    mChart.setData(data);
                    mChart.invalidate();
                    mChart.getDescription().setText(categorySpinner.getSelectedItem().toString() + " along the day");
                }
            }
        });
    }

    private void initCalendar() {
        currentDate = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        currentDate.setTime(new Date());
        final String initialDateText = currentDate.get(Calendar.YEAR) + "/" + (currentDate.get(Calendar.MONTH + 1) < 10 ? "0" + (currentDate.get(Calendar.MONTH) + 1) :
                (currentDate.get(Calendar.MONTH) + 1))+ "/" + (currentDate.get(Calendar.DATE) < 10 ? "0" + (currentDate.get(Calendar.DATE)) : (currentDate.get(Calendar.DATE)));
        selectedDate = initialDateText;
        readingDateText.setText(initialDateText);
        getDayReadings(initialDateText);
    }
}
