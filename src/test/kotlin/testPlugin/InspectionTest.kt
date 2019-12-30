package testPlugin

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.pom.java.LanguageLevel
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase
import com.intellij.util.PathUtil
import fr.yopox.test_problem.JavaInvokeLaterInspection
import org.junit.Test
import java.io.File

class InspectionTest : JavaCodeInsightFixtureTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.testDataPath = "src/test/testData"
    }

    override fun tuneFixture(moduleBuilder: JavaModuleFixtureBuilder<*>) {
        super.tuneFixture(moduleBuilder)
        moduleBuilder.addJdk("/Library/Java/JavaVirtualMachines/adoptopenjdk-13.jdk/Contents/Home")
        moduleBuilder.setLanguageLevel(LanguageLevel.JDK_13)
    }

    private fun doTest(name: String, expected: Boolean) {
        myFixture.configureByFile("$name.java")
        myFixture.enableInspections(JavaInvokeLaterInspection())
        val highlight = myFixture.doHighlighting().filter { it.severity == HighlightSeverity.ERROR }
        for (h in highlight) {
            println(h.description)
        }
        assert(highlight.isNotEmpty() == expected)
    }

    @Test fun test1() = doTest("javaFalse1", false)
    @Test fun test2() = doTest("javaFalse2", false)

    @Test fun test3() = doTest("javaTrue1", true)
    @Test fun test4() = doTest("javaTrue2", true)
    @Test fun test5() = doTest("javaTrue3", true)
    @Test fun test6() = doTest("javaTrue4", true)

}