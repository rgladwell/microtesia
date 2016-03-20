// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import scala.annotation.implicitNotFound
import microtesia.MicrodataValue
import scala.util.Try

/**
 * Trait for implicit formatters to de-serialize microdata values to hard types.
 * You can define custom formats as follows:
 * 
 * {{{
 * scala> import microtesia._
 * import microtesia._
 * 
 * scala> import formats._
 * import formats._
 * 
 * scala> import java.time._
 * import java.time._
 *
 * scala> import scala.util.{Failure, Success, Try}
 * import scala.util.{Failure, Success, Try}
 * 
 * scala> implicit val dateFormat = new MicrodataFormat[LocalDate] {
 *      |  override def read(microdata: MicrodataValue): Try[LocalDate] = microdata match {
 *      |    case MicrodataString(value)  => Success(LocalDate.parse(value))
 *      |    case other                   => Failure(CannotConvert(classOf[LocalDate], other))
 *      |  }
 *      | }
 * dateFormat: microtesia.formats.MicrodataFormat[java.time.LocalDate] = $anon$1@4fa4e3b6
 * 
 * scala> MicrodataString("2016-01-01").convertTo[LocalDate]
 * res0: Try[java.time.LocalDate] = Success(2016-01-01)
 * }}}
 */
@implicitNotFound(msg = "Cannot find MicrodataFormat type class for ${T}")
trait MicrodataFormat[T] {

  def read(microdata: MicrodataValue): Try[T]

}
