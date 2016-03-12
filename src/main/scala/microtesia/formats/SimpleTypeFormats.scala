// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

/**
 * Implicit microdata formats for simple types, including [[String]] or value
 * types like [[Int]]. This needs to be in scope to support automatic conversion
 * of these types. It is include as part of the [[formats]] package object.
 */
trait SimpleTypeFormats {

  import ValueReads._

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[Int]].
   */
  implicit val defaultIntFormat: MicrodataFormat[Int] = new SimpleFormat[Int]

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[Double]].
   */
  implicit val defaultDoubleFormat: MicrodataFormat[Double] = new SimpleFormat[Double]

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[Float]].
   */
  implicit val defaultFloatFormat: MicrodataFormat[Float] = new SimpleFormat[Float]

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[Long]].
   */
  implicit val defaultLongFormat: MicrodataFormat[Long] = new SimpleFormat[Long]

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[Short]].
   */
  implicit val defaultShortFormat: MicrodataFormat[Short] = new SimpleFormat[Short]

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[Boolean]].
   */
  implicit val defaultBooleanFormat: MicrodataFormat[Boolean] = new SimpleFormat[Boolean]

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata values to [[String]].
   */
  implicit val defaultStringFormat: MicrodataFormat[String] = new SimpleFormat[String]

}