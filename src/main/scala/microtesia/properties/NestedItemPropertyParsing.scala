// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import microtesia.{ItemsParser, Tag}

private[microtesia] trait NestedItemPropertyParsing[N] extends PropertyParsing[N] {
  this: ItemsParser[N] =>

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse{
    case element @ Tag(_) if element.hasAttr("itemscope") => parseItem(element)
  }

}
