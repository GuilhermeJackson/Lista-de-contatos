package com.example.minhaagenda.util;

import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class MascarasUtil {
    public static void colocaMascara(TextView view, String mascara){
        SimpleMaskFormatter maskFormatter = new SimpleMaskFormatter(mascara);
        MaskTextWatcher watcher = new MaskTextWatcher(view, maskFormatter);
        view.addTextChangedListener(watcher);

    }
}
