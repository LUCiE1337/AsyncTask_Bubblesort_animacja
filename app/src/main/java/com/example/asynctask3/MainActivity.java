package com.example.asynctask3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumElements;
    private Button buttonSort;
    private ProgressBar progressBar;
    private BarChartView barChartView;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNumElements = findViewById(R.id.editTextNumElements);
        buttonSort = findViewById(R.id.buttonSort);
        progressBar = findViewById(R.id.progressBar);
        barChartView = findViewById(R.id.barChartView);

        buttonSort.setOnClickListener(view -> {
            String input = editTextNumElements.getText().toString();
            if (!input.isEmpty()) {
                int numElements = Integer.parseInt(input);
                startSorting(numElements);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSorting(int numElements) {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < numElements; i++) {
            data.add((int) (Math.random() * 100));
        }

        buttonSort.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        executor.submit(() -> {
            List<Integer> sortedData = bubbleSort(data, (updatedData, progress) -> {
                handler.post(() -> {
                    barChartView.setData(updatedData);
                    progressBar.setProgress(progress);
                });
            });

            handler.post(() -> {
                progressBar.setVisibility(View.GONE);
                buttonSort.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Sorting completed!", Toast.LENGTH_SHORT).show();
                barChartView.setData(sortedData); // Final sorted data
            });
        });
    }

    private List<Integer> bubbleSort(List<Integer> data, SortProgressListener listener) {
        List<Integer> array = new ArrayList<>(data);
        int size = array.size();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (array.get(j) > array.get(j + 1)) {
                    int temp = array.get(j);
                    array.set(j, array.get(j + 1));
                    array.set(j + 1, temp);
                }
            }
            int progress = (i * 100) / (size - 1);
            listener.onProgress(array, progress);
        }

        return array;
    }

    interface SortProgressListener {
        void onProgress(List<Integer> updatedData, int progress);
    }
}
