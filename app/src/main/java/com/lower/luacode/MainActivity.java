package com.lower.luacode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import androidx.appcompat.app.AppCompatActivity;

import com.lower.luacode.editor.LuaHighlighter;
import com.lower.luacode.editor.LineNumberHelper;

public class MainActivity extends AppCompatActivity {

    private EditText editor;
    private TextView lineNumbers, terminal;
    private LuaHighlighter highlighter;
    private LineNumberHelper lineHelper;

    private Globals luaGlobals;

    private LinearLayout layoutEditor, layoutTerminal;
    private Button btnEditor, btnTerminal, btnRodar;

    private boolean isHighlighting = false;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        lineNumbers = findViewById(R.id.lineNumbers);
        highlighter = new LuaHighlighter();
        lineHelper = new LineNumberHelper(editor, lineNumbers);

        layoutEditor = findViewById(R.id.layoutEditor);
        layoutTerminal = findViewById(R.id.layoutTerminal);

        btnEditor = findViewById(R.id.btnEditor);
        btnTerminal = findViewById(R.id.btnTerminal);
        btnRodar = findViewById(R.id.btnRodar);
        
        terminal = findViewById(R.id.console);

        mostrarEditor();

        btnEditor.setOnClickListener(v -> mostrarEditor());
        btnTerminal.setOnClickListener(v -> mostrarTerminal());
        btnRodar.setOnClickListener(v -> runLua());

        editor.setText("function mostrar(s)\n\tprint(a)\nend");
        editor.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

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
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> lineNumbers.scrollTo(0, scrollY));
        initLua();        
    }

    private void mostrarEditor() {
        layoutEditor.setVisibility(LinearLayout.VISIBLE);
        layoutTerminal.setVisibility(LinearLayout.GONE);
        btnEditor.setBackgroundColor(0xFF444444);
        btnTerminal.setBackgroundColor(0xFF222222);
    }

    private void mostrarTerminal() {
        layoutEditor.setVisibility(LinearLayout.GONE);
        layoutTerminal.setVisibility(LinearLayout.VISIBLE);
        btnEditor.setBackgroundColor(0xFF222222);
        btnTerminal.setBackgroundColor(0xFF444444);
    }
    private void initLua() {
        luaGlobals = JsePlatform.standardGlobals();
        luaGlobals.set(
                "print",
                new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg) {
                        terminal.append(arg.tojstring() + "\n");
                        return LuaValue.NIL;
                    }
                });
    }

    private void runLua() {
        mostrarTerminal();
        String code = editor.getText().toString();
        terminal.setText("");
        try {
            LuaValue chunk = luaGlobals.load(code);
            chunk.call();
        } catch (LuaError e) {
            terminal.append("Error: " + e.getMessage() + "\n");
        }
    }
}