package fr.smarquis.applinks

import android.content.Context
import android.text.SpannableStringBuilder
import java.util.Deque
import android.text.Spanned
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.LeadingMarginSpan
import java.util.Arrays
import android.os.Bundle
import fr.smarquis.applinks.R
import java.util.ArrayDeque

/**
 * Inspired by JakeWharton's Truss
 * Original file at https://gist.github.com/JakeWharton/11274467
 *
 *
 * Licensed under Apache License, Version 2.0
 */
internal class Printer(context: Context) {

    private val tabWidth: Int = context.resources.getDimensionPixelSize(R.dimen.tab_width)
    private val builder: SpannableStringBuilder= SpannableStringBuilder()
    private val stack: Deque<Span> = ArrayDeque()

    private class Span(val start: Int, val span: Any)

    private fun append(string: String?): Printer {
        if (string != null) {
            builder.append(string)
        }
        return this
    }

    private fun append(charSequence: CharSequence?): Printer {
        if (charSequence != null) {
            builder.append(charSequence)
        }
        return this
    }

    private fun append(c: Char): Printer {
        builder.append(c)
        return this
    }

    private fun append(number: Int): Printer {
        builder.append(number.toString())
        return this
    }

    /**
     * Starts `span` at the current position in the builder.
     */
    private fun pushSpan(span: Any): Printer {
        stack.addLast(Span(builder.length, span))
        return this
    }

    /**
     * End the most recently pushed span at the current position in the builder.
     */
    private fun popSpan(): Printer {
        val span = stack.removeLast()
        builder.setSpan(span.span, span.start, builder.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * Create the final [CharSequence], popping any remaining spans.
     */
    fun build(): CharSequence {
        while (!stack.isEmpty()) {
            popSpan()
        }
        return builder
    }

    fun style(style: Int): Printer {
        return pushSpan(StyleSpan(style))
    }

    fun bold(): Printer {
        return pushSpan(StyleSpan(Typeface.BOLD))
    }

    fun italic(): Printer {
        return pushSpan(StyleSpan(Typeface.ITALIC))
    }

    fun size(proportion: Float): Printer {
        return pushSpan(RelativeSizeSpan(proportion))
    }

    fun small(): Printer {
        return pushSpan(RelativeSizeSpan(0.8f))
    }

    fun small(proportion: Float): Printer {
        return pushSpan(RelativeSizeSpan(proportion))
    }

    fun tab(): Printer {
        return pushSpan(LeadingMarginSpan.Standard(tabWidth))
    }

    fun newLine(): Printer {
        return small(0.5f).append('\n').popSpan()
    }

    fun newLine(count: Int): Printer {
        var count = count
        small(0.5f)
        while (count-- > 0) {
            append('\n')
        }
        return popSpan()
    }

    fun stripNewLines(): Printer {
        while (builder.length > 0 && builder[builder.length - 1] == '\n') {
            builder.delete(builder.length - 1, builder.length)
        }
        return this
    }

    fun appendKeyValue(key: String?, value: Any?): Printer {
        return appendKey(key).newLine().appendValue(value).newLine(2)
    }

    fun appendKey(key: String?): Printer {
        return bold().append(if (key == null || key.isEmpty()) ZWSP else key).popSpan()
    }

    fun appendValue(value: Any?): Printer {
        if (value == null) {
            append(EMPTY)
        } else if (value is Set<*>) {
            append(value.toString())
        } else if (value is Array<*>) {
            append(value.toString())
        } else if (value is Map<*, *>) {
            if (value.isEmpty()) {
                append(EMPTY)
            } else {
                for ((key, value1) in value) {
                    val k = key.toString()
                    val v = value1.toString()
                    tab().small().appendKeyValue(k, v).popSpan().popSpan()
                }
                stripNewLines()
            }
        } else {
            val string = value.toString()
            append(string.ifEmpty { ZWSP })
        }
        return this
    }

    fun appendBundle(bundle: Bundle?): Printer {
        if (bundle == null) {
            return this
        }
        for (key in bundle.keySet()) {
            appendKey(key)
            val value = bundle[key]
            if (value == null) {
                newLine().append(EMPTY).newLine(2)
                continue
            }
            val clazz: Class<*> = value.javaClass
            appendAsClass(clazz).newLine()
            if (clazz.isArray) {
                val type = clazz.componentType
                when (type) {
                    Long::class.javaPrimitiveType -> append(Arrays.toString(value as LongArray?))
                    Int::class.javaPrimitiveType -> append(Arrays.toString(value as IntArray?))
                    Char::class.javaPrimitiveType -> append(Arrays.toString(value as CharArray?))
                    Boolean::class.javaPrimitiveType -> append(Arrays.toString(value as BooleanArray?))
                    Byte::class.javaPrimitiveType -> append(Arrays.toString(value as ByteArray?))
                    Float::class.javaPrimitiveType -> append(Arrays.toString(value as FloatArray?))
                    Short::class.javaPrimitiveType -> append(Arrays.toString(value as ShortArray?))
                    Double::class.javaPrimitiveType -> append(Arrays.toString(value as DoubleArray?))
                    Long::class.javaPrimitiveType -> append(Arrays.toString(value as LongArray?))
                    else -> append(value.toString())
                }
            } else if (value is Bundle) {
                tab().appendBundle(value as Bundle?).popSpan()
                continue
            } else {
                val string = value.toString()
                append(string.ifEmpty { ZWSP })
            }
            newLine(2)
        }
        return this
    }

    private fun appendAsClass(clazz: Class<*>): Printer {
        return italic().small(0.5f)
            .append("   as ").append(clazz.simpleName)
            .popSpan().popSpan()
    }

    companion object {
        const val EMPTY = "\u2205"
        const val ZWSP = "\uFEFF"
    }

}