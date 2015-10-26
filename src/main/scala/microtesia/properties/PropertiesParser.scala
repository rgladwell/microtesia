// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.xml.Node
import microtesia.{Element, InvalidMicrodata, Parsed, MicrodataProperty}

private[microtesia] trait PropertiesParser[N] {
  this: PropertyParsing[N] =>

  def parseProperties(element: Element[N]): Parsed[Seq[MicrodataProperty], N] = {

    def validatePropertyNames(p: Option[String]): Parsed[Seq[String], N] =
      p match {
        case Some(value) => Right(value.split(" +"))
        case _           => Left(
                              InvalidMicrodata[N](
                                "The itemprop attribute's value must have at least one token.",
                                element
                              )
                            )
      }

    element match {

      case e if e.hasAttr("itemprop") => for {
                                           names <- validatePropertyNames(e.attr("itemprop")).right
                                           value <- parseProperty(e).right
                                         } yield(
                                           names.map{ MicrodataProperty(_, value) }
                                         )

      case _                          => element
                                           .childMap { parseProperties( _ ) }
                                           .traverse[Seq[MicrodataProperty]](Nil)(_ ++ _)

    }

  }

}
