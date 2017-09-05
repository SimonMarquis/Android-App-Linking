package fr.smarquis.applinks;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.LeadingMarginSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

/**
 * Inspired by JakeWharton's Truss
 * Original file at https://gist.github.com/JakeWharton/11274467
 * <p>
 * Licensed under Apache License, Version 2.0
 */

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "SameParameterValue", "unused"})
class Printer {

    static final String EMPTY = "\u2205";
    static final String ZWSP = "\uFEFF";

    private final int tabWidth;

    private final SpannableStringBuilder builder;

    private final Deque<Span> stack;

    private static final class Span {
        final int start;
        final Object span;

        private Span(int start, Object span) {
            this.start = start;
            this.span = span;
        }
    }

    public Printer(Context context) {
        builder = new SpannableStringBuilder();
        stack = new ArrayDeque<>();
        tabWidth = context.getResources().getDimensionPixelSize(R.dimen.tab_width);
    }

    private Printer append(String string) {
        if (string != null) {
            builder.append(string);
        }
        return this;
    }

    private Printer append(CharSequence charSequence) {
        if (charSequence != null) {
            builder.append(charSequence);
        }
        return this;
    }

    private Printer append(char c) {
        builder.append(c);
        return this;
    }

    private Printer append(int number) {
        builder.append(String.valueOf(number));
        return this;
    }

    /**
     * Starts {@code span} at the current position in the builder.
     */
    private Printer pushSpan(Object span) {
        stack.addLast(new Span(builder.length(), span));
        return this;
    }

    /**
     * End the most recently pushed span at the current position in the builder.
     */
    private Printer popSpan() {
        Span span = stack.removeLast();
        builder.setSpan(span.span, span.start, builder.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * Create the final {@link CharSequence}, popping any remaining spans.
     */
    public CharSequence build() {
        while (!stack.isEmpty()) {
            popSpan();
        }
        return builder;
    }

    public Printer style(int style) {
        return pushSpan(new StyleSpan(style));
    }

    public Printer bold() {
        return pushSpan(new StyleSpan(Typeface.BOLD));
    }

    public Printer italic() {
        return pushSpan(new StyleSpan(Typeface.ITALIC));
    }

    public Printer size(float proportion) {
        return pushSpan(new RelativeSizeSpan(proportion));
    }

    public Printer small() {
        return pushSpan(new RelativeSizeSpan(0.8f));
    }

    public Printer small(float proportion) {
        return pushSpan(new RelativeSizeSpan(proportion));
    }

    public Printer tab() {
        return pushSpan(new LeadingMarginSpan.Standard(tabWidth));
    }

    public Printer newLine() {
        return small(0.5f).append('\n').popSpan();
    }

    public Printer newLine(int count) {
        small(0.5f);
        while (count-- > 0) {
            append('\n');
        }
        return popSpan();
    }

    public Printer stripNewLines() {
        while (builder.length() > 0 && builder.charAt(builder.length() - 1) == '\n') {
            builder.delete(builder.length() - 1, builder.length());
        }
        return this;
    }

    public Printer appendKeyValue(String key, Object value) {
        return appendKey(key).newLine().appendValue(value).newLine(2);
    }

    public Printer appendKey(String key) {
        return bold().append(key == null || key.isEmpty() ? ZWSP : key).popSpan();
    }

    public Printer appendValue(Object value) {
        if (value == null) {
            append(EMPTY);
        } else if (value instanceof Set) {
            Set set = (Set) value;
            Object[] array = set.toArray();
            append(Arrays.toString(array));
        } else if (value instanceof Object[]) {
            append(Arrays.toString((Object[]) value));
        } else if (value instanceof Map<?, ?>) {
            if (((Map) value).isEmpty()) {
                append(EMPTY);
            } else {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                    String k = String.valueOf(entry.getKey());
                    String v = String.valueOf(entry.getValue());
                    tab().small().appendKeyValue(k, v).popSpan().popSpan();
                }
                stripNewLines();
            }
        } else {
            String string = value.toString();
            append(string.isEmpty() ? ZWSP : string);
        }
        return this;
    }

    public Printer appendBundle(@Nullable Bundle bundle) {
        if (bundle == null) {
            return this;
        }
        for (String key : bundle.keySet()) {
            appendKey(key);
            Object value = bundle.get(key);
            if (value == null) {
                newLine().append(EMPTY).newLine(2);
                continue;
            }
            Class<?> clazz = value.getClass();
            appendAsClass(clazz).newLine();
            if (clazz.isArray()) {
                Class<?> type = clazz.getComponentType();
                if (type == long.class) {
                    append(Arrays.toString((long[]) value));
                } else if (type == int.class) {
                    append(Arrays.toString((int[]) value));
                } else if (type == char.class) {
                    append(Arrays.toString((char[]) value));
                } else if (type == boolean.class) {
                    append(Arrays.toString((boolean[]) value));
                } else if (type == byte.class) {
                    append(Arrays.toString((byte[]) value));
                } else if (type == float.class) {
                    append(Arrays.toString((float[]) value));
                } else if (type == short.class) {
                    append(Arrays.toString((short[]) value));
                } else if (type == double.class) {
                    append(Arrays.toString((double[]) value));
                } else if (type == long.class) {
                    append(Arrays.toString((long[]) value));
                } else {
                    append(Arrays.toString((Object[]) value));
                }
            } else if (value instanceof Bundle) {
                tab().appendBundle((Bundle) value).popSpan();
                continue;
            } else {
                String string = value.toString();
                append(string.isEmpty() ? ZWSP : string);
            }
            newLine(2);
        }
        return this;
    }

    private Printer appendAsClass(Class<?> clazz) {
        return italic().small(0.5f)
                .append("   as ").append(clazz.getSimpleName())
                .popSpan().popSpan();
    }


}
