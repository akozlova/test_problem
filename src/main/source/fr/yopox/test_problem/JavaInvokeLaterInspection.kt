package fr.yopox.test_problem

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import fr.yopox.test_problem.JavaInvokeLaterInspection.JavaVisitor
import fr.yopox.test_problem.JavaInvokeLaterInspection.JavaVisitor.Companion.forbiddenCall

/**
 * Adds an inspection for [JavaVisitor.forbiddenCall] in Java code.
 */
class JavaInvokeLaterInspection : AbstractBaseJavaLocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = JavaVisitor(holder)

    /**
     * Registers a problem if [forbiddenCall] is used.
     */
    private class JavaVisitor(val holder: ProblemsHolder) : JavaElementVisitor() {

        companion object {
            const val forbiddenCall = "javax.swing.SwingUtilities.invokeLater"
            const val description = "[Java] Using '${forbiddenCall}' is prohibited"
            val possibleImports = arrayListOf<String>()

            // Builds [possibleImports] : it can be possible to stop at each dot of [forbiddenCall].
            init {
                for ((i, c) in forbiddenCall.withIndex()) {
                    if (c == '.')
                        possibleImports.add(forbiddenCall.subSequence(0, i).toString())
                }
                possibleImports.add(forbiddenCall)
            }
        }

        val forbiddenExpressions = arrayListOf(forbiddenCall)

        /**
         * Parses the [PsiImportList] and builds [forbiddenExpressions].
         */
        override fun visitImportList(list: PsiImportList?) {
            super.visitImportList(list)

            // [forbiddenCall] should be at least package.Class.fun
            if (possibleImports.size < 3) return

            val regex = Regex("import [^/]*[*]")

            list?.allImportStatements?.forEach { import ->
                val name = import.importReference?.qualifiedName ?: return@forEach
                if (name in possibleImports) {

                    // Package imported : Class.fun is forbidden
                    if (regex.containsMatchIn(import.text))
                        forbiddenExpressions.addIfAbsent(
                            forbiddenCall.substring(
                                possibleImports[possibleImports.lastIndex - 2].length + 1,
                                forbiddenCall.length
                            )
                        )

                    // Class / Static fun imported : Class.fun / fun is forbidden
                    else {
                        val index = possibleImports.indexOf(name)
                        if (index > 0)
                            forbiddenExpressions.addIfAbsent(
                                forbiddenCall.substring(
                                    possibleImports[index - 1].length + 1,
                                    forbiddenCall.length
                                )
                            )

                    }
                }
            }
        }

        override fun visitMethod(method: PsiMethod?) {
            super.visitMethod(method)
        }

        /**
         * Looks for forbidden expressions in a [PsiReferenceExpression].
         */
        override fun visitReferenceExpression(expression: PsiReferenceExpression?) {
            super.visitReferenceExpression(expression)

            // Match forbidden expressions
            if (expression?.text in forbiddenExpressions) {
                expression?.lastChild?.let { holder.registerProblem(it, description) }
            }

        }
    }
}

fun ArrayList<String>.addIfAbsent(element: String) {
    if (element !in this) this.add(element)
}