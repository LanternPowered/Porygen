/*
 * Porygen
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.porygen.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Root() {
  MaterialTheme {
    Column {
      Menu()
      Row {

      }
    }
  }
}

@Composable
fun Menu() {
  var active by remember { mutableStateOf(false) }
  TopAppBar {
    DropdownMenu(
      toggle = {
        Button(
          onClick = { active = !active },
          modifier = Modifier.padding(start = 5.dp, top = 10.dp),
          content = { Text("File") }
        )
      },
      expanded = active,
      onDismissRequest = { active = false },
      dropdownContent = {
        DropdownMenuItem(
          onClick = {
            println("Save")
          },
          content = {
            Text("Save")
          }
        )
      }
    )
  }
}
