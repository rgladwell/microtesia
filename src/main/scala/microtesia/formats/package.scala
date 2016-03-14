// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.util.Try

/**
 * API to automatically de-serialise [[MicrodataValue]] instances into value types and case classes.
 *
 * == Usage ==
 *
 * To use simply put the Microtesia Formats API in scope (with the Microtesia API). This enriches
 * [[MicrodataValue]] instances with the [[EnrichedMicrodataValue.convertTo]] method which you can
 * use to parse microdata as follows:
 *
 * {{{
 * scala> import microtesia._
 * import microtesia._
 *
 * scala> import formats._
 * import formats._
 *
 * scala> case class Person(name: String, age: Int, adult: Boolean)
 * defined class Person
 * 
 * scala>  MicrodataItem(
 *           Seq(
 *             ("name", MicrodataString("hello")),
 *             ("age", MicrodataString("13")),
 *             ("adult", MicrodataString("true"))
 *           )
 *         ).convertTo[Person]
 * res0: Try[Person] = Success(Person(hello,13,true))
 * }}}
 */
package object formats extends SimpleTypeFormats with RichTypeFormats with CollectionFormats with ShapelessFormats {

  /**
   * Implicit typeclass to enrich [[MicrodataValue]] instances with the [[convertTo]] method.
   */
  implicit class EnrichedMicrodataValue(value: MicrodataValue) {

    /**
     * Automatically de-serialize [[MicrodataValue]] to simple and value types, as well as case classes.
     * E.g.:
     * 
     * {{{
     * scala> import microtesia._
     * import microtesia._
     * 
     * scala> import formats._
     * import formats._
     * 
     * scala> MicrodataString("10").convertTo[Int]
     * res0: Try[Int] = Success(10)
     * }}}
     */
    def convertTo[T](implicit format: MicrodataFormat[T]): Try[T] = format.read(value)
  }

}
