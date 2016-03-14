// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.util.{Failure, Success, Try}
import scala.xml.Node
import microtesia.{Element, InvalidMicrodata, MicrodataProperty}

private[microtesia] trait PropertiesParser[N] {
  this: PropertyParsing[N] =>

  def parseProperties(element: Element[N]): Try[Seq[MicrodataProperty]] = {

    def validatePropertyNames(p: Option[String]): Try[Seq[String]] =
      p match {
        case Some(value) => Success(value.split(" +"))
        case _           => Failure(
                              InvalidMicrodata[N](
                                "The itemprop attribute's value must have at least one token.",
                                element
                              )
                            )
      }

    element match {

      case e if e.hasAttr("itemprop") => for {
                                           names <- validatePropertyNames(e.attr("itemprop"))
                                           value <- parseProperty(e)
                                         } yield(
                                           names.map{ MicrodataProperty(_, value) }
                                         )

      case _                          => element
                                           .childMap { parseProperties( _ ) }
                                           .traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)

    }

  }

}
