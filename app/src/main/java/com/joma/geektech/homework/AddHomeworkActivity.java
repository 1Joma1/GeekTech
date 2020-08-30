package com.joma.geektech.homework;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.joma.geektech.R;
import com.joma.geektech.model.Homework;
import com.joma.geektech.model.User;
import com.joma.geektech.util.Utils;

import java.util.List;

public class AddHomeworkActivity extends AppCompatActivity {

    private EditText title, task1, task2, task3, task4, task5, task6, task7, task8, task9, task10;
    private Button save;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);
        user = Utils.getUser(this);
        findView();
        listener();
    }

    private void findView() {
        title = findViewById(R.id.add_homework_task_lesson);
        task1 = findViewById(R.id.add_homework_task_1);
        task2 = findViewById(R.id.add_homework_task_2);
        task3 = findViewById(R.id.add_homework_task_3);
        task4 = findViewById(R.id.add_homework_task_4);
        task5 = findViewById(R.id.add_homework_task_5);
        task6 = findViewById(R.id.add_homework_task_6);
        task7 = findViewById(R.id.add_homework_task_7);
        task8 = findViewById(R.id.add_homework_task_8);
        task9 = findViewById(R.id.add_homework_task_9);
        task10 = findViewById(R.id.add_homework_task_10);
        save = findViewById(R.id.add_homework_add);
    }

    private void listener() {
        save.setOnClickListener(view -> {
            if (Utils.getText(title).length() < 1) {
                title.setError(getResources().getString(R.string.must_fill));
            } else {
                Homework homework = new Homework();
                homework.setLesson(Utils.getText(title));
                homework.setGroup(user.getGroup());
                if (Utils.getText(task1).length() > 1) {
                    homework.addTask(Utils.getText(task1));
                }
                if (Utils.getText(task2).length() > 1) {
                    homework.addTask(Utils.getText(task2));
                }
                if (Utils.getText(task3).length() > 1) {
                    homework.addTask(Utils.getText(task3));
                }
                if (Utils.getText(task4).length() > 1) {
                    homework.addTask(Utils.getText(task4));
                }
                if (Utils.getText(task5).length() > 1) {
                    homework.addTask(Utils.getText(task5));
                }
                if (Utils.getText(task6).length() > 1) {
                    homework.addTask(Utils.getText(task6));
                }
                if (Utils.getText(task7).length() > 1) {
                    homework.addTask(Utils.getText(task7));
                }
                if (Utils.getText(task8).length() > 1) {
                    homework.addTask(Utils.getText(task8));
                }
                if (Utils.getText(task9).length() > 1) {
                    homework.addTask(Utils.getText(task9));
                }
                if (Utils.getText(task10).length() > 1) {
                    homework.addTask(Utils.getText(task10));
                }
                FirebaseFirestore.getInstance().collection("Homework").add(homework);
                Toast.makeText(AddHomeworkActivity.this, getResources().getString(R.string.added), Toast.LENGTH_SHORT).show();
                title.setText("");
                task1.setText("");
                task2.setText("");
                task3.setText("");
                task4.setText("");
                task5.setText("");
                task6.setText("");
                task7.setText("");
                task8.setText("");
                task9.setText("");
                task10.setText("");
            }
        });
    }
}
