package com.example.listview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText numberInput1, numberInput2;
    private Button calculateButton;
    private ListView resultListView;
    private ArrayList<String> resultsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        numberInput1 = findViewById(R.id. numberInput1);
        numberInput2 = findViewById(R.id.numberInput2);
        calculateButton = findViewById(R.id.calculateButton);
        resultListView = findViewById(R.id.resultListView);

        resultsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultsList);
        resultListView.setAdapter(adapter);

        setupKeyboardVisibilityListener(this, new OnKeyboardVisibilityListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean keyboardVisible) {
                if (keyboardVisible) {
                    adjustViewsForKeyboard();
                } else {
                    restoreViews();
                }
            }
        });

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                showToast(selectedItem);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void adjustViewsForKeyboard() {
        // Ajusta las vistas según tus necesidades
    }

    private void restoreViews() {
        // Restaura las vistas a su posición original
    }

    private void performCalculation() {
        String num1Str = numberInput1.getText().toString();
        String num2Str = numberInput2.getText().toString();

        if (!num1Str.isEmpty() && !num2Str.isEmpty()) {
            double num1 = Double.parseDouble(num1Str);
            double num2 = Double.parseDouble(num2Str);
            double result = num1 + num2;

            String calculation = num1Str + " + " + num2Str + " = " + result;
            resultsList.add(calculation);
            adapter.notifyDataSetChanged();

            numberInput1.setText("");
            numberInput2.setText("");

            hideKeyboard();
        }
    }

    public static void setupKeyboardVisibilityListener(Activity activity, OnKeyboardVisibilityListener listener) {
        final View contentView = activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect rect = new Rect();
            private boolean wasOpened = false;
            @Override
            public void onGlobalLayout() {
                contentView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = contentView.getHeight();
                int keypadHeight = screenHeight - rect.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    if (!wasOpened) {
                        wasOpened = true;
                        listener.onKeyboardVisibilityChanged(true);
                    }
                } else {
                    if (wasOpened) {
                        wasOpened = false;
                        listener.onKeyboardVisibilityChanged(false);
                    }
                }
            }
        });
    }

    public interface OnKeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean keyboardVisible);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
