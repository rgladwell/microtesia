// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.util.Success
import microtesia.{MicrodataString, Tag}

private[microtesia] trait DataPropertyParsing[N] extends PropertyParsing[N] {

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse{
    case element @ Tag("data") => Success(MicrodataString(element.attr("value").getOrElse("")))
  }

}
