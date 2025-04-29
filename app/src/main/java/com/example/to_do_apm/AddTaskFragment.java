package com.example.to_do_apm;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Spinner spinnerPriority;
    private TextView textViewDate;
    private FloatingActionButton saveTaskButton;
    private Calendar calendar;
    private Task taskToEdit;
    private int editPosition = -1;

    public AddTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        editTextTitle = rootView.findViewById(R.id.editTextTitle);
        editTextDescription = rootView.findViewById(R.id.editTextDescription);
        spinnerPriority = rootView.findViewById(R.id.spinnerPriority);
        textViewDate = rootView.findViewById(R.id.textViewDate);
        saveTaskButton = rootView.findViewById(R.id.saveTaskButton);

        calendar = Calendar.getInstance();

        String[] priorities = {"Low", "Medium", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        editTextTitle.setText("");
        editTextDescription.setText("");
        textViewDate.setText("Select Date");
        spinnerPriority.setSelection(0);
        Log.d("AddTaskFragment", "Fields reset for new task");

        Bundle args = getArguments();
        if (args != null && args.containsKey("task")) {
            taskToEdit = (Task) args.getSerializable("task");
            editPosition = args.getInt("position", -1);
            if (taskToEdit != null) {
                editTextTitle.setText(taskToEdit.getTitle());
                editTextDescription.setText(taskToEdit.getDescription());
                textViewDate.setText(taskToEdit.getDate());
                spinnerPriority.setSelection(adapter.getPosition(taskToEdit.getPriority()));
                Log.d("AddTaskFragment", "Editing task: " + taskToEdit.getTitle());
            }
        } else {
            Log.d("AddTaskFragment", "No task to edit, new task mode");
        }

        textViewDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
            datePickerDialog.updateDate(year, month, day);
            datePickerDialog.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                textViewDate.setText(sdf.format(calendar.getTime()));
            });
            datePickerDialog.show();
        });

        saveTaskButton.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String priority = spinnerPriority.getSelectedItem().toString();
            String date = textViewDate.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty() && !date.equals("Select Date")) {
                if (taskToEdit != null && editPosition != -1) {
                    // Update existing task
                    taskToEdit.setTitle(title);
                    taskToEdit.setDescription(description);
                    taskToEdit.setDate(date);
                    taskToEdit.setPriority(priority);
                    Log.d("AddTaskFragment", "Updated task: " + title);
                } else {
                    Task newTask = new Task(title, description, date, priority);
                    TaskManager.addTask(newTask);
                    Log.d("AddTaskFragment", "Added new task: " + title);
                }
                taskToEdit = null;
                editPosition = -1;
                NavHostFragment.findNavController(AddTaskFragment.this)
                        .navigate(R.id.action_addTaskFragment_to_taskListFragment);
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AddTaskFragment", "Fragment destroyed");
    }
}