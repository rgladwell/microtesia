// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import scala.annotation.implicitNotFound
import microtesia.MicrodataValue

/**
 * Trait for implicit formatters to de-serialize microdata properties.
 * Mostly used internally.
 */
@implicitNotFound(msg = "Cannot find MicrodataPropertyFormat type class for ${T}")
trait MicrodataPropertyFormat[T] {

  def read(properties: Seq[MicrodataValue]): Either[CannotConvert, T]

}
