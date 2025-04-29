package com.example.to_do_apm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ConstraintLayout emptyStateContainer;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButtonNonEmpty;
    private TaskAdapter taskAdapter;

    public TaskListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        emptyStateContainer = rootView.findViewById(R.id.emptyStateContainer);
        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButtonNonEmpty = rootView.findViewById(R.id.floatingActionButtonNonEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(TaskManager.getTasks());
        recyclerView.setAdapter(taskAdapter);

        updateUI();

        floatingActionButton.setOnClickListener(v -> {
            Log.d("TaskListFragment", "Empty state FAB clicked");
            NavHostFragment.findNavController(TaskListFragment.this)
                    .navigate(R.id.action_taskListFragment_to_addTaskFragment);
        });

        floatingActionButtonNonEmpty.setOnClickListener(v -> {
            Log.d("TaskListFragment", "Non-empty state FAB clicked");
            NavHostFragment.findNavController(TaskListFragment.this)
                    .navigate(R.id.action_taskListFragment_to_addTaskFragment);
        });

        return rootView;
    }

    private void updateUI() {
        if (TaskManager.getTasks().isEmpty()) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            floatingActionButtonNonEmpty.setVisibility(View.GONE);
        } else {
            emptyStateContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            floatingActionButtonNonEmpty.setVisibility(View.VISIBLE);
            taskAdapter.notifyDataSetChanged();
        }
    }

    class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task task = tasks.get(position);
            holder.textViewTitle.setText(task.getTitle());
            holder.textViewDescription.setText(task.getDescription());
            holder.textViewDate.setText(task.getDate());
            holder.textViewPriority.setText("Priority: " + task.getPriority());
            holder.checkBox.setChecked(task.isChecked());

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                TaskManager.toggleTaskStatus(task);
                notifyItemChanged(position);
                updateUI();
            });

            holder.buttonDelete.setOnClickListener(v -> {
                TaskManager.removeTask(task);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tasks.size());
                updateUI();
                Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
            });

            holder.buttonModify.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                bundle.putInt("position", position);
                NavHostFragment.findNavController(TaskListFragment.this)
                        .navigate(R.id.action_taskListFragment_to_addTaskFragment, bundle);
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView textViewTitle, textViewDescription, textViewDate, textViewPriority;
            CheckBox checkBox;
            Button buttonDelete, buttonModify;

            public TaskViewHolder(View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewDescription = itemView.findViewById(R.id.textViewDescription);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                textViewPriority = itemView.findViewById(R.id.textViewPriority);
                checkBox = itemView.findViewById(R.id.checkBox);
                buttonDelete = itemView.findViewById(R.id.buttonDelete);
                buttonModify = itemView.findViewById(R.id.buttonModify);
            }
        }
    }
}