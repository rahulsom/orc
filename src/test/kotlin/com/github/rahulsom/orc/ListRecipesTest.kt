package com.github.rahulsom.orc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListRecipesTest {
  @Test
  fun shouldListRecipes() {
    val recipes = ListRecipes().getRecipes()

    assertThat(recipes).isNotNull()
  }
}
