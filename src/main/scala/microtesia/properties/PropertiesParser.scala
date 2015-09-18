// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.properties

import scala.xml.Node
import microtesia.{Element, InvalidMicrodata, Parsed, Properties}

private[microtesia] trait PropertiesParser[N] {
  this: PropertyParsing[N] =>

  def parseProperties(element: Element[N]): Parsed[Properties, N] = {

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

      case element if element.hasAttr("itemprop") => for {
                                                       names <- validatePropertyNames(element.attr("itemprop")).right
                                                       value <- parseProperty(element).right
                                                     } yield(
                                                       names.map{ (_, Seq(value)) }
                                                     )
                                                     .toMap

      case _                                       => element
                                                        .childMap { parseProperties( _ ) }
                                                        .traverse[Properties](Map())(_ merge _)

    }

  }

}
