# Test problem

Write inspection to detect `pack.ages.Class.fun`

## Java inspection

The array `forbiddenExpressions` is built according to the import statements :

1. The full call `pack.ages.Class.fun` is forbidden
1. If a package is imported (`import pack.*;` / `import pack.….ages.*;`), the call `Class.fun` is forbidden
1. If the class is imported (`import pack.ages.Class;`), the call `Class.fun` is forbidden
1. If the function is imported (`import static pack.ages.Class.fun;`), the call `fun` is forbidden

## Kotlin inspection (obsolete)

A `AbstractUastNonRecursiveVisitor` looking for the following `UExpression` node is setup :

1. `sourcePsi` is a `REFERENCE_EXPRESSION` corresponding to `invokeLater`
1. the "parent" `sourcePsi?.parent?.parent?.firstChild?.lastChild` is a `REFERENCE_EXPRESSION` corresponding to `SwingUtilities`

![Kotlin PSI tree](assets/kt_tree.png)
