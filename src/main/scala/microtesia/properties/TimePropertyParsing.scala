// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.xml.Node
import microtesia.{MicrodataString, Tag}
import scala.util.Success

private[microtesia] trait TimePropertyParsing[N] extends PropertyParsing[N] {

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse {

    case element @ Tag("time") if (element.attr("datetime").isDefined) => Success(MicrodataString(element.attr("datetime").get))
    case element @ Tag("time") if (element.value.nonEmpty) => Success(MicrodataString(element.value))
    case element @ Tag("time") => Success(MicrodataString(""))

  }

}
