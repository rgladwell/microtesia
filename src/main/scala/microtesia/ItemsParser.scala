// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.net.URI
import microtesia.properties.PropertiesParser

private[microtesia] trait ItemsParser[N] {
  this: PropertiesParser[N] =>

  def parseItems(element: Element[N]): Parsed[Seq[MicrodataItem], N] =
    element match {

      case element if element.hasAttr("itemscope") => parseItem(element).right.map { Seq(_) }

      case _                                  => element
                                                   .childMap{ parseItems(_) }
                                                   .traverse[Seq[MicrodataItem]](Nil)(_ ++ _)

    }

  def parseItem(element: Element[N]): Parsed[MicrodataItem, N] = {
    val properties = element
                       .childMap{ parseProperties(_) }
                       .traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)

    val props = Seq(properties, parseItemReferences(element)).traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)

    props
      .right
      .map{ p =>
        MicrodataItem(
          itemtype   = element.attr("itemtype").map(new URI(_)),
          properties = p,
          id         = element.attr("itemid").map(new URI(_))
        )
      }
  }

  private def parseItemReferences(element: Element[N]): Parsed[Seq[MicrodataProperty], N] =
    element.attr("itemref")
      .map {
        _.split(" +")
        .toSeq
        .map {
          parseItemReference(_, element)
        }
        .traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)
      }
    .getOrElse(Right(Seq()))

  private def parseItemReference(id: String, element: Element[N]): Parsed[Seq[MicrodataProperty], N] = {
    element.doc.findById(id).map { parseProperties(_) }.getOrElse(Right(Seq()))
  }

}
