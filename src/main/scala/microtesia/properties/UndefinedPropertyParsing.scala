// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

private[microtesia] class UndefinedPropertyParsing[N] extends PropertyParsing[N] {

  override def parseProperty: PropertyParser[N] = undefinedPropertyParser

}
