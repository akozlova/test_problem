package fr.yopox.test_problem

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReferenceExpression

/**
 * Adds an inspection for [javax.swing.SwingUtilities.invokeLater] in Java code.
 */
class JavaInvokeLaterInspection : AbstractBaseJavaLocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = JavaVisitor(holder)

    private class JavaVisitor(val holder: ProblemsHolder) : JavaElementVisitor() {

        val description = "[Java] Using 'SwingUtilities.invokeLater' is prohibited"

        /**
         * Looks for SwingUtilities.invokeLater in a [PsiReferenceExpression].
         */
        override fun visitReferenceExpression(expression: PsiReferenceExpression?) {
            super.visitReferenceExpression(expression)

            // Discard non matching expressions
            if (!(expression?.firstChild?.lastChild?.text == "SwingUtilities" && expression.lastChild?.text == "invokeLater")) return

            holder.registerProblem(expression.lastChild, description)
        }

    }

}