package com.example.to_do_apm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment {
    private EditText editTextTitle, editTextDescription;
    private TextView textViewDate;
    private FloatingActionButton saveTaskButton;
    private Calendar calendar;

    public AddTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        editTextTitle = rootView.findViewById(R.id.editTextTitle);
        editTextDescription = rootView.findViewById(R.id.editTextDescription);
        textViewDate = rootView.findViewById(R.id.textViewDate);
        saveTaskButton = rootView.findViewById(R.id.saveTaskButton);

        calendar = Calendar.getInstance();

        // Set up DatePickerDialog for textViewDate
        textViewDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        textViewDate.setText(sdf.format(calendar.getTime()));
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        saveTaskButton.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String date = textViewDate.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty() && !date.equals("Select Date")) {
                Task newTask = new Task(title, description, date);
                TaskManager.addTask(newTask);
                NavHostFragment.findNavController(AddTaskFragment.this)
                        .navigate(R.id.action_addTaskFragment_to_taskListFragment);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}