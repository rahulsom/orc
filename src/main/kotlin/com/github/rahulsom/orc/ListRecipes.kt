package com.github.rahulsom.orc

import org.openrewrite.config.Environment
import picocli.CommandLine

@CommandLine.Command(name = "list", mixinStandardHelpOptions = true)
class ListRecipes : Runnable {
  override fun run() {
    getRecipes()
      .forEach { (k, v) ->
        println("")
        println(k)
        println(k.replace(Regex("."), "="))
        v.sortedBy { it.split(".").last() }
          .forEach {
            val name = it.split(".").last()
            println("${name.padEnd(50)} = $it")
          }
      }
  }

  fun getRecipes() = Environment.builder().scanRuntimeClasspath().build().listRecipes()
    .map { it.name }
    .groupBy { it.split('.').dropLast(1).joinToString(".") }
    .toSortedMap()
}
