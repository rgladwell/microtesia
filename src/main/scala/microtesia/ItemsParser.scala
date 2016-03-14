// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.net.URI
import microtesia.properties.PropertiesParser
import scala.util.{Success, Try}

private[microtesia] trait ItemsParser[N] {
  this: PropertiesParser[N] =>

  def parseItems(element: Element[N]): Try[Seq[MicrodataItem]] =
    element match {

      case e if e.hasAttr("itemscope") => parseItem(element).map { Seq(_) }

      case _                           => element
                                            .childMap{ parseItems(_) }
                                            .traverse[Seq[MicrodataItem]](Nil)(_ ++ _)

    }

  def parseItem(element: Element[N]): Try[MicrodataItem] = {
    val properties = element
                       .childMap{ parseProperties(_) }
                       .traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)

    val props = Seq(properties, parseItemReferences(element)).traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)

    props
      .map{ p =>
        MicrodataItem(
          itemtype   = element.attr("itemtype").map(new URI(_)),
          properties = p,
          id         = element.attr("itemid").map(new URI(_))
        )
      }
  }

  private def parseItemReferences(element: Element[N]): Try[Seq[MicrodataProperty]] =
    element.attr("itemref")
      .map {
        _.split(" +")
        .toSeq
        .map {
          parseItemReference(_, element)
        }
        .traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)
      }
    .getOrElse(Success(Seq()))

  private def parseItemReference(id: String, element: Element[N]): Try[Seq[MicrodataProperty]] = {
    element.doc.findById(id).map { parseProperties(_) }.getOrElse(Success(Seq()))
  }

}
