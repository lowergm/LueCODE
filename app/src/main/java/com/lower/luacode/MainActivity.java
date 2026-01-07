package com.lower.luacode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lower.luacode.editor.LuaHighlighter;
import com.lower.luacode.editor.LineNumberHelper;

public class MainActivity extends AppCompatActivity {

    private EditText editor;
    private TextView lineNumbers;

    private LuaHighlighter highlighter;
    private LineNumberHelper lineHelper;

    private boolean isHighlighting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        lineNumbers = findViewById(R.id.lineNumbers);

        highlighter = new LuaHighlighter();
        lineHelper = new LineNumberHelper(editor, lineNumbers);

        lineHelper.update();

        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isHighlighting) return;

                isHighlighting = true;
                lineHelper.update();
                highlighter.highlight(s);
                isHighlighting = false;
            }
        });

        editor.setOnScrollChangeListener(
                (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                        lineNumbers.scrollTo(0, scrollY)
        );
    }
}