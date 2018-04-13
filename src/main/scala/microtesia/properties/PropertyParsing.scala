// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

private[microtesia] abstract class PropertyParsing[N] {

  def parseProperty: PropertyParser[N]

}
