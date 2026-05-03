package com.example.diabetesriskanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment {

    private RadioGroup q1, q2, q3, q4, q5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        q1 = view.findViewById(R.id.q1Group);
        q2 = view.findViewById(R.id.q2Group);
        q3 = view.findViewById(R.id.q3Group);
        q4 = view.findViewById(R.id.q4Group);
        q5 = view.findViewById(R.id.q5Group);
        Button analyzeBtn = view.findViewById(R.id.analyzeBtn);

        analyzeBtn.setOnClickListener(v -> {
            int score = calculateTotal(view);
            if (score == 0) {
                Toast.makeText(getContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
                return;
            }

            String risk = (score <= 7) ? "LOW" : (score <= 14) ? "MEDIUM" : "HIGH";

            Intent intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra("risk", risk);
            intent.putExtra("score", score);
            startActivity(intent);
        });

        return view;
    }

    private int calculateTotal(View view) { // Pass the view here
        int[] groups = {R.id.q1Group, R.id.q2Group, R.id.q3Group, R.id.q4Group, R.id.q5Group};
        int total = 0;
        for (int id : groups) {
            RadioGroup rg = view.findViewById(id); // Use the passed view
            int checked = rg.getCheckedRadioButtonId();
            if (checked == -1) return 0;
            total += (rg.indexOfChild(view.findViewById(checked)) + 1);
        }
        return total;
    }


    // Add a public method for the Activity to call for Reset
    // Rename this method from resetFields to resetAll
    public void resetAll() {
        if (q1 != null) {
            q1.clearCheck();
            q2.clearCheck();
            q3.clearCheck();
            q4.clearCheck();
            q5.clearCheck();
        }
    }
}
