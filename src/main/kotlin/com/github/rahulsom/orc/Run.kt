package com.github.rahulsom.orc

import org.openrewrite.InMemoryExecutionContext
import org.openrewrite.config.Environment
import org.openrewrite.java.JavaParser
import picocli.CommandLine
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.io.path.writeText

@CommandLine.Command(name = "run", mixinStandardHelpOptions = true)
class Run : Runnable {
  @CommandLine.Option(names = ["-p", "--project"], description = ["Path to project to run recipe on"])
  var projectDir: Path = Paths.get(".")

  @CommandLine.Option(names = ["-r", "--recipe"], description = ["Recipe to run"])
  var recipes: List<String> = listOf()

  override fun run() {
    println("Running recipe(s) ${recipes} on project ${projectDir}")
    val classpath = emptyList<Path>()

    val environment = Environment.builder().scanRuntimeClasspath().build()

    val recipe = environment.activateRecipes(recipes)

    val javaParser = JavaParser.fromJavaVersion()
      .classpath(classpath)
      .build()

    val sourcePaths = Files
      .find(projectDir, 999, { _, bfa -> bfa.isRegularFile })
      .collect(Collectors.toList())

    val ctx = InMemoryExecutionContext(Throwable::printStackTrace)

    val cus = javaParser.parse(sourcePaths, projectDir, ctx)

    val results = recipe.run(cus, ctx).results

    results.forEach { result ->
      result.after?.let { projectDir.resolve(it.sourcePath).writeText(it.printAll()) }
    }

  }
}
