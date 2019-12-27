package fr.yopox.test_problem

import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.uast.UastVisitorAdapter
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.visitor.AbstractUastNonRecursiveVisitor

/**
 * Adds an inspection for [javax.swing.SwingUtilities.invokeLater] in Kotlin code.
 */
class KotlinInvokeLaterInspection : AbstractBaseUastLocalInspectionTool() {

    val description = "[Kt] Using 'SwingUtilities.invokeLater' is prohibited"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return UastVisitorAdapter(object : AbstractUastNonRecursiveVisitor() {

            /**
             * Looks for *.SwingUtilities.invokeLater in a [UExpression].
             */
            override fun visitExpression(node: UExpression): Boolean {

                // Child node : invokeLater
                if (node.sourcePsi?.text == "invokeLater") {

                    // Parent node : *.SwingUtilities
                    val parent = node.sourcePsi?.parent?.parent?.firstChild?.lastChild

                    // Register the problem
                    if (parent?.text == "SwingUtilities")
                        node.sourcePsi?.let { holder.registerProblem(it, description) }
                }

                return super.visitExpression(node)
            }

        }, true)
    }

}