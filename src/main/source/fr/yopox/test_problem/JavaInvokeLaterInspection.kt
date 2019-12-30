package fr.yopox.test_problem

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.impl.compiled.ClsClassImpl
import com.intellij.psi.impl.compiled.ClsMethodImpl
import com.intellij.psi.impl.java.stubs.impl.PsiClassStubImpl
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
            val functionName = forbiddenCall.split('.').last()
        }

        /**
         * Looks for forbidden expressions in a [PsiReferenceExpression].
         */
        override fun visitReferenceExpression(expression: PsiReferenceExpression?) {
            super.visitReferenceExpression(expression)

            if (expression?.lastChild?.text != functionName) return

            // Match forbidden expressions
            val resolved = expression.resolve()
            if (resolved is ClsMethodImpl) {
                val stub = resolved.stub.parentStub
                if (stub is PsiClassStubImpl) {
                    if ("${stub.getQualifiedName()}.$functionName" == forbiddenCall) {
                        holder.registerProblem(expression, description)
                    }
                }
            }

        }
    }
}