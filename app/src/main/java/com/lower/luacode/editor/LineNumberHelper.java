package com.lower.luacode.editor;

import android.widget.EditText;
import android.widget.TextView;

public class LineNumberHelper {

    private final EditText editor;
    private final TextView lineNumbers;

    public LineNumberHelper(EditText editor, TextView lineNumbers) {
        this.editor = editor;
        this.lineNumbers = lineNumbers;
    }

    public void update() {
        int lineCount = editor.getLineCount();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append("\n");
        }

        lineNumbers.setText(sb.toString());
    }
}