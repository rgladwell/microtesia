// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.util.Try

package object properties {

  private[microtesia] type PropertyParser[N] = PartialFunction[Element[N], Try[MicrodataValue]]

  private[properties] val undefinedPropertyParser: PartialFunction[Any, Nothing] = Map.empty

}
