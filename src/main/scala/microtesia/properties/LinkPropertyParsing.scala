// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.util.{Failure, Success}
import microtesia.{InvalidMicrodata, MicrodataLink, Tag}
import java.net.URI

private[microtesia] trait LinkPropertyParsing[N] extends PropertyParsing[N] {

  val links = "^(a|area|link)".r

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse{
    case element @ Tag(links(_)) => {

      if (element.hasAttr("href")) Success(MicrodataLink(new URI(element.attr("href").get)))
      else Failure(
        InvalidMicrodata[N](
          "If the itemprop is specified on an a or area element, then the href attribute must also be specified.",
          element
        )
      )

    }
  }

}
