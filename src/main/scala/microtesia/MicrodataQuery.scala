// Copyright 2018 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.annotation.tailrec

/**
 * Provides support for XPath-like microdata querying.
 */
trait MicrodataQuery {

  /**
   * Results found for this query.
   */
  val results: Seq[MicrodataValue]

  private case class QueryResults(results: Seq[MicrodataValue]) extends MicrodataQuery

  /**
   * Query item properties using XPath-like syntax.
   *
   * For example:
   *
   * {{{
   * scala> val item = MicrodataItem(properties = Seq(("name", MicrodataString("Brian"))))
   *
   * scala> item \ "name"
   * res1: MicrodataQuery = QueryResults(List(MicrodataString(Brian)))
   * }}}
   */
  def \(key: String): MicrodataQuery = {
    val newResults =
      for {
        MicrodataItem(properties, _, _) <- results
        (name, value) <- properties if name == key
      } yield value

    QueryResults(newResults)
  }

  /**
   * Recursively query item properties using XPath-like syntax.
   *
   * For example:
   *
   * {{{
   * scala> val item = MicrodataItem(properties = Seq(("friend", MicrodataItem(properties = Seq(("name", MicrodataString("Brian")))))))
   *
   * scala> item \\ "name"
   * res1: MicrodataQuery = QueryResults(List(MicrodataString(Brian)))
   * }}}
   */
  def \\(key: String): MicrodataQuery = {

    @tailrec
    def recursiveQuery(toquery: List[MicrodataValue], found: Seq[MicrodataValue] = Nil): Seq[MicrodataValue] =
      toquery match {
        case Nil => found
        case (x: MicrodataItem) :: xs => recursiveQuery(xs ++ x.children, found ++ (x \ key).results)
        case x :: xs => recursiveQuery(xs, found)
      }

    QueryResults(recursiveQuery(results.toList))
  }

}
