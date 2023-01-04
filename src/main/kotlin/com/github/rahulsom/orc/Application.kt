package com.github.rahulsom.orc

import picocli.CommandLine

@CommandLine.Command(
  name = "openrewrite", mixinStandardHelpOptions = true, subcommands = [Run::class, ListRecipes::class]
)
class Application : Runnable {
  override fun run() {
    CommandLine.usage(this, System.out)
  }
}

fun main(args: Array<String>) {
  CommandLine(Application()).execute(*args)
}
