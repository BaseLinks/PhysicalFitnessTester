package com.kangear.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class InputFilterUtils {
    public static class MinMax implements InputFilter {
        private int min, max;

        public MinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String oldVal = "";
                // Remove the string out of destination that is to be replaced
                String newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend, dest.toString().length());
                // Add the new string in
                newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(dstart, newVal.length());
                //****Add this line (below) to allow Negative values***//
                if (newVal.equalsIgnoreCase("-") && min < 0) return null;
                int input = Integer.parseInt(newVal);
                Log.i(TAG, "InputFilterUtils: oldVal: " + source.toString() + " newVal: " + newVal);
                if (isInRange(min, max, input))
                    return null;
                else {
                    // Buzzer
                    if (newVal.length() > 0) {
                        oldVal = newVal.substring(0, newVal.length() - 1);
                    }
                    return oldVal;
                }
            } catch (NumberFormatException nfe) {
                // nfe.printStackTrace();
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}