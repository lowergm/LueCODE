package com.lower.luacode.editor;

import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuaHighlighter {

    private static final int COLOR_KEYWORD = 0xFF569CD6;
    private static final int COLOR_COMMENT = 0xFF6A9955;
    private static final int COLOR_STRING  = 0xFFD69D85;

    private static final Pattern PATTERN_KEYWORD =
            Pattern.compile("\\b(function|end|if|then|else|elseif|for|while|do|local|return|break)\\b");

    private static final Pattern PATTERN_COMMENT =
            Pattern.compile("--.*?$", Pattern.MULTILINE);

    private static final Pattern PATTERN_STRING =
            Pattern.compile("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'");

    public void highlight(Editable editable) {

        // Remove spans antigos
        ForegroundColorSpan[] spans =
                editable.getSpans(0, editable.length(), ForegroundColorSpan.class);

        for (ForegroundColorSpan span : spans) {
            editable.removeSpan(span);
        }

        applyPattern(editable, PATTERN_KEYWORD, COLOR_KEYWORD);
        applyPattern(editable, PATTERN_COMMENT, COLOR_COMMENT);
        applyPattern(editable, PATTERN_STRING, COLOR_STRING);
    }

    private void applyPattern(Editable editable, Pattern pattern, int color) {
        Matcher matcher = pattern.matcher(editable);
        while (matcher.find()) {
            editable.setSpan(
                    new ForegroundColorSpan(color),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }
}