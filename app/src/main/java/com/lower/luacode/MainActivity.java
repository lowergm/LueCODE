package com.lower.luacode;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // ===== CORES =====
    private static final int COLOR_KEYWORD = 0xFF569CD6;
    private static final int COLOR_COMMENT = 0xFF6A9955;
    private static final int COLOR_STRING  = 0xFFD69D85;

    // ===== REGEX =====
    private static final Pattern PATTERN_KEYWORD =
            Pattern.compile(
                    "\\b(function|end|if|then|else|elseif|for|while|do|local|return|break)\\b"
            );

    private static final Pattern PATTERN_COMMENT =
            Pattern.compile("--.*?$", Pattern.MULTILINE);

    private static final Pattern PATTERN_STRING =
            Pattern.compile("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'");

    private boolean isHighlighting = false;

    private EditText editor;
    private TextView lineNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        lineNumbers = findViewById(R.id.lineNumbers);

        // Atualiza números de linha inicialmente
        updateLineNumbers();

        editor.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after
            ) {}

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count
            ) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isHighlighting) return;

                isHighlighting = true;

                updateLineNumbers();
                highlightLua(s);

                isHighlighting = false;
            }
        });

        // Sincroniza scroll do editor com os números
        editor.setOnScrollChangeListener(
                (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                        lineNumbers.scrollTo(0, scrollY)
        );
    }

    // ===== HIGHLIGHT LUA =====
    private void highlightLua(Editable editable) {

        // Remove spans antigos
        ForegroundColorSpan[] spans =
                editable.getSpans(
                        0,
                        editable.length(),
                        ForegroundColorSpan.class
                );

        for (ForegroundColorSpan span : spans) {
            editable.removeSpan(span);
        }

        // Keywords
        Matcher keywordMatcher = PATTERN_KEYWORD.matcher(editable);
        while (keywordMatcher.find()) {
            editable.setSpan(
                    new ForegroundColorSpan(COLOR_KEYWORD),
                    keywordMatcher.start(),
                    keywordMatcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // Comentários
        Matcher commentMatcher = PATTERN_COMMENT.matcher(editable);
        while (commentMatcher.find()) {
            editable.setSpan(
                    new ForegroundColorSpan(COLOR_COMMENT),
                    commentMatcher.start(),
                    commentMatcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // Strings
        Matcher stringMatcher = PATTERN_STRING.matcher(editable);
        while (stringMatcher.find()) {
            editable.setSpan(
                    new ForegroundColorSpan(COLOR_STRING),
                    stringMatcher.start(),
                    stringMatcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    // ===== NÚMEROS DE LINHA =====
    private void updateLineNumbers() {
        int lineCount = editor.getLineCount();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append("\n");
        }

        lineNumbers.setText(sb.toString());
    }
}