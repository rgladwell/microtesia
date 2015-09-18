// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.xml.Node
import microtesia.{MicrodataString, Tag}

private[microtesia] trait TimePropertyParsing[N] extends PropertyParsing[N] {

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse{

    case element @ Tag("time") => {

      if (element.attr("datetime").isDefined)  Right(MicrodataString(element.attr("datetime").get))
      else if (element.value.nonEmpty)         Right(MicrodataString(element.value))
      else                                     Right(MicrodataString(""))

    }

  }

}
