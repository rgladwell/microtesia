// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

package object properties {

  private[microtesia] type PropertyParser[N] = PartialFunction[Element[N], Parsed[MicrodataValue, N]]

  private[properties] val Undefined: PartialFunction[Any, Nothing] = Map.empty

}
