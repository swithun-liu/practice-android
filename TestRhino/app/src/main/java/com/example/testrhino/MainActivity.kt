package com.example.testrhino

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.faendir.rhino_android.RhinoAndroidHelper
import org.mozilla.javascript.Callable
import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.ImporterTopLevel
import org.mozilla.javascript.Scriptable
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private var context: Context? = null
    private var scope: Scriptable? = null
    private var rhinoAndroidHelper: RhinoAndroidHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rhinoAndroidHelper = RhinoAndroidHelper(this)
        context = rhinoAndroidHelper!!.enterContext()
        context!!.optimizationLevel = 1
        scope = ImporterTopLevel(context)
    }


    private fun toastScript(script: String) {
        try {
            val result = context!!.evaluateString(scope, script, "<hello_world>", 1, null)
            Toast.makeText(this, Context.toString(result), Toast.LENGTH_LONG).show()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun button(v: View) {
        when (v.id) {
            R.id.button -> toastScript((findViewById<View>(R.id.editText) as EditText).text.toString())
//            R.id.button3 -> OptimizationComparisonTask(this, rhinoAndroidHelper!!).execute(-1, 0, 1)
            R.id.button3 -> runMathJax()
        }
    }

    // 加载 MathJax 的 JavaScript 文件
    private fun loadMathJaxLibrary() {
        val mathJaxScript = InputStreamReader(this.resources.openRawResource(R.raw.mathjax))
        context!!.evaluateReader(scope, mathJaxScript, "mathjax.js", 1, null)
    }

    private fun runMathJax() {
        loadMathJaxLibrary()
        val latexFormula = "\\frac{1}{2} \\pi r^2"
        renderLatexToSvg(latexFormula)
    }

    // 渲染 LaTeX 公式并返回 SVG 内容
    fun renderLatexToSvg(latexFormula: String): String {
        val script = """
            var MathJax = {
                Hub: {
                    Queue: function(args) {
                        return MathJax.Hub.Typeset(args[0]);
                    },
                    Typeset: function(element) {
                        return MathJax.Hub.getAllJax(element)[0].getSVG();
                    }
                }
            };
            
            // 渲染 LaTeX 公式
            var element = document.createElement('div');
            element.innerHTML = '$latexFormula';
            return MathJax.Hub.Queue([element]);
        """.trimIndent()

        val svgResult = context!!.evaluateString(scope, script, "renderLatexToSvg", 1, null)
        return svgResult.toString()
    }

    private fun runJavascript(number: Int): Int {
        // 创建一个 JavaScript 执行环境
        val context = Context.enter()
        return try {
            // 创建一个空的 JavaScript scope
            val scope: Scriptable = context.initStandardObjects()

            // 定义一个 JavaScript 函数：输入数字返回数字 + 10
            val script = """
                function addTen(num) {
                    return num + 10;
                }
            """

            // 执行脚本
            context.evaluateString(scope, script, "<cmd>", 1, null)

            // 获取 JavaScript 函数对象
            val addTenFunction = scope.get("addTen", scope) as Function

            // 调用 JavaScript 函数
            val result = addTenFunction.call(context, scope, scope, arrayOf<Any>(number))

            // 将结果转换为整数并返回
            Context.toNumber(result).toInt().also {
                Toast.makeText(this, "result: $it", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
                .also {
                    Toast.makeText(this, "result: err", Toast.LENGTH_LONG).show()
                }
            0 // 出现错误时返回 0
        } finally {
            // 离开执行上下文
            Context.exit()
        }
    }

}