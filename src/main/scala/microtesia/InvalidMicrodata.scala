// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

/**
 * Represents failure to parse microdata document, including the [[line]] and [[column]]
 * number of the failing HTML element.
 * 
 * @param message Descriptive message of the parsing error.
 * @param line Line number in the document of the HTML failing validation.
 * @param column Column number in the document of the HTML failing validation.
 */
case class InvalidMicrodata private (message: String, line: Option[Int], column: Option[Int])

private object InvalidMicrodata {

  def apply[N](message: String, element: Element[N]): InvalidMicrodata = {

    def attribute(name: String) = element.attr(name).map { _.toInt }

    def tagsoupColumnErrorAdjustment(column: Int) = column - 60

    val line: Option[Int] = attribute("line")
    val column: Option[Int] = attribute("column").map { tagsoupColumnErrorAdjustment }

    InvalidMicrodata(message, line, column)

  }

}
