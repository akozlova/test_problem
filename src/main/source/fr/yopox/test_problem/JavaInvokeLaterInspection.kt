package fr.yopox.test_problem

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.InspectionsBundle
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReferenceExpression

/**
 * Adds an inspection for SwingUtilities.invokeLater in Java code.
 */
class JavaInvokeLaterInspection : AbstractBaseJavaLocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = JavaVisitor(holder)

    private class JavaVisitor(val holder: ProblemsHolder) : JavaElementVisitor() {

        val description = "Using 'SwingUtilities.invokeLater' is prohibited"

        /**
         * Looks for SwingUtilities.invokeLater in [PsiReferenceExpression]s.
         */
        override fun visitReferenceExpression(expression: PsiReferenceExpression?) {
            super.visitReferenceExpression(expression)

            // Discard non SwingUtilities.invokeLater or *.SwingUtilities.invokeLater expressions
            if (!(expression?.firstChild?.lastChild?.text == "SwingUtilities" && expression.lastChild?.text == "invokeLater")) return

            holder.registerProblem(expression, description)
        }

    }

}