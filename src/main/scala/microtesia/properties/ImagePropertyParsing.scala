// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import microtesia.{InvalidMicrodata, MicrodataLink, Tag}
import java.net.URI

private[microtesia] trait ImagePropertyParsing[N]  extends PropertyParsing[N] {

  val embeds = "^(img|audio|embed|iframe|source|track|video)".r

  abstract override def parseProperty: PropertyParser[N] = super.parseProperty.orElse {

    case element @ Tag(embeds(_)) => {

      if (element.hasAttr("src")) Right(MicrodataLink(new URI(element.attr("src").get)))

      else
        Left(
          InvalidMicrodata[N](
            "If the itemprop is specified on a media element, then the src attribute must also be specified.",
            element
          )
        )

    }

  }

}
