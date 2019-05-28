package com.novintech.elevator.util;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;

public class FontUtils {

    public static SpannableString typeface(Typeface typeface, CharSequence string) {
        SpannableString s = new SpannableString(string);
        s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }
}