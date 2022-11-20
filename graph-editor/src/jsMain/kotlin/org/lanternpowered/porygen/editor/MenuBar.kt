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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

data class MenuItem(
  val content: @Composable () -> Unit,
  val subItems: List<MenuItem>? = null,
  val onClick: (() -> Unit)? = null
)

@Composable
fun MenuBar(
  items: List<MenuItem>,
  attrs: AttrBuilderContext<HTMLDivElement>? = null,
) {
  Div(
    attrs = {
      attrs?.invoke(this)
    },
    content = {
      var active: Int? by remember { mutableStateOf(null) }
      for ((index, menuItem) in items.withIndex()) {
        // var popupOffset by remember { mutableStateOf(Offset.Zero) }
        val buttonPadding = 10.px
        if (!menuItem.subItems.isNullOrEmpty()) {
          Div(
            attrs = {
              style {
                position(Position.Relative)
                display(DisplayStyle.InlineBlock)
                padding(buttonPadding)
                if (index != 0)
                  paddingLeft(0.px)
              }
            },
            content = {
              Button(
                attrs = {
                  onMouseEnter {
                    active = index
                  }
                  onMouseLeave {
                    active = null
                  }
                },
                content = {
                  menuItem.content()
                }
              )
              Div(
                attrs = {
                  style {
                    display(if (active == index) DisplayStyle.Block else DisplayStyle.None)
                    position(Position.Absolute)
                  }
                  onMouseEnter {
                    active = index
                  }
                  onMouseLeave {
                    active = null
                  }
                },
                content = {
                  for (subItem in menuItem.subItems) {
                    Button(
                      attrs = {
                        onClick {
                          subItem.onClick?.invoke()
                        }
                      },
                      content = {
                        subItem.content()
                      }
                    )
                  }
                }
              )
            }
          )
        } else {
          Button(
            attrs = {
              onClick {
                menuItem.onClick?.invoke()
              }
              style {
                padding(buttonPadding)
                if (index != 0)
                  paddingLeft(0.px)
              }
            },
            content = {
              menuItem.content()
            }
          )
        }
      }
    }
  )
}
