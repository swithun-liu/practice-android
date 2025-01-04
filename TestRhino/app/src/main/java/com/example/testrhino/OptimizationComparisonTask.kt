package com.example.testrhino

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.faendir.rhino_android.RhinoAndroidHelper
import org.mozilla.javascript.ImporterTopLevel
import org.mozilla.javascript.Scriptable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.util.stream.Collectors
import java.util.stream.LongStream

/**
 * @author F43nd1r
 * @since 06.02.18
 */
internal class OptimizationComparisonTask(
    context: Context,
    private val rhinoAndroidHelper: RhinoAndroidHelper
) : AsyncTask<Int?, Void?, List<OptimizationComparisonTask.Result>>() {
    private val tableParams = TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT
    )
    private val rowParams = TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
    )
    private val progressDialog = ProgressDialog(context)
    private val context = WeakReference(context)
    private val code: String

    init {
        val builder = StringBuilder()
        try {
            val codeStream = context.resources.openRawResource(R.raw.compute_pi)
            val codeReader = BufferedReader(InputStreamReader(codeStream))
            builder.ensureCapacity(codeStream.available())
            var line: String?
            while ((codeReader.readLine().also { line = it }) != null) {
                builder.append(line).append('\n')
            }
        } catch (ignored: IOException) {
        }
        this.code = builder.toString()
    }

    protected override fun doInBackground(vararg params: Int): List<Result> {
        val context = rhinoAndroidHelper.enterContext()
        val scope: Scriptable = ImporterTopLevel(context)
        //warm up
        for (i in params) {
            context.optimizationLevel = i
            for (j in 0..9) {
                context.compileString(code, "compute_pi", 1, null)
            }
        }
        val times: MutableList<Result> = ArrayList()
        for (i in params) {
            context.optimizationLevel = i
            val start = System.nanoTime()
            val script = context.compileString(code, "compute_pi", 1, null)
            val compilation = System.nanoTime() - start
            val execution = LongStream.generate {
                val s = System.nanoTime()
                script.exec(context, scope)
                System.nanoTime() - s
            }.skip(5).limit(25).average().orElse(1.0).toLong()
            times.add(Result(i, compilation / 1000, execution / 1000))
        }
        org.mozilla.javascript.Context.exit()
        return times
    }

    override fun onPreExecute() {
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun onPostExecute(times: List<Result>) {
        progressDialog.dismiss()
        val context = context.get()
        if (context != null) {
            val tableLayout = TableLayout(context)
            tableLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tableLayout.dividerDrawable = ColorDrawable(Color.GRAY)
            tableLayout.showDividers = TableLayout.SHOW_DIVIDER_MIDDLE
            val baseCompilationTime =
                times.stream().mapToLong { obj: Result -> obj.compilationTime }
                    .max().orElse(1)
            val baseExecutionTime = times.stream().mapToLong { obj: Result -> obj.executionTime }
                .max().orElse(1)
            val baseSum = times.stream().mapToLong { obj: Result -> obj.sum }
                .max().orElse(1)
            val percentages = times.stream().map { result: Result ->
                result.asPercentageOf(
                    baseCompilationTime,
                    baseExecutionTime,
                    baseSum
                )
            }
                .collect(Collectors.toList())
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Optimization",
                    *times.stream().mapToLong { obj: Result -> obj.optimizationLevel.toLong() }
                        .toArray()))
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Compilation (μs)",
                    *times.stream().mapToLong { obj: Result -> obj.compilationTime }
                        .toArray()))
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Compilation (%)",
                    *percentages.stream().mapToLong { obj: Result -> obj.compilationTime }
                        .toArray()))
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Execution (μs)",
                    *times.stream().mapToLong { obj: Result -> obj.executionTime }
                        .toArray()))
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Execution (%)",
                    *percentages.stream().mapToLong { obj: Result -> obj.executionTime }
                        .toArray()))
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Sum (μs)",
                    *times.stream().mapToLong { obj: Result -> obj.sum }
                        .toArray()))
            tableLayout.addView(
                getRowWithTexts(
                    context,
                    "Sum (%)",
                    *percentages.stream().mapToLong { obj: Result -> obj.sum }
                        .toArray()))
            val scrollView = HorizontalScrollView(context)
            scrollView.addView(tableLayout)
            AlertDialog.Builder(context).setView(scrollView).setPositiveButton("Close", null).show()
        }
    }

    private fun getTextViewForText(context: Context, text: String): TextView {
        val textView = TextView(context)
        textView.layoutParams = rowParams
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setPadding(5, 0, 5, 0)
        return textView
    }

    private fun getRowWithTexts(context: Context, title: String, vararg values: Long): TableRow {
        val tableRow = TableRow(context)
        tableRow.layoutParams = tableParams
        tableRow.dividerDrawable = ColorDrawable(Color.GRAY)
        tableRow.showDividers = TableLayout.SHOW_DIVIDER_MIDDLE
        val titleView = getTextViewForText(context, title)
        titleView.setTypeface(null, Typeface.BOLD)
        tableRow.addView(titleView)
        for (value in values) {
            tableRow.addView(getTextViewForText(context, value.toString()))
        }
        return tableRow
    }

    internal class Result private constructor(
        val optimizationLevel: Int,
        val compilationTime: Long,
        val executionTime: Long,
        val sum: Long
    ) {
        constructor(optimizationLevel: Int, compilationTime: Long, executionTime: Long) : this(
            optimizationLevel,
            compilationTime,
            executionTime,
            compilationTime + executionTime
        )

        fun asPercentageOf(compilationTime: Long, executionTime: Long, sum: Long): Result {
            return Result(
                optimizationLevel,
                this.compilationTime * 100 / compilationTime,
                this.executionTime * 100 / executionTime,
                this.sum * 100 / sum
            )
        }
    }
}
