// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import microtesia.{MicrodataString, Tag}

private[microtesia] trait StringPropertyParsing[N] extends PropertyParsing[N] {

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse{
    case element @ Tag(_) => Right(MicrodataString(element.value))
  }

}
