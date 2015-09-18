// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import microtesia.{MicrodataValue, Parsed}

private[microtesia] abstract class PropertyParsing[N] {

  def parseProperty: PropertyParser[N]

}
