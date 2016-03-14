// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import microtesia._
import scala.util.{Failure, Success, Try}

private class SimpleFormat[V](implicit read: Read[V]) extends MicrodataFormat[V] {
  override def read(microdata: MicrodataValue): Try[V] = microdata match {
    case MicrodataString(value)  => Success(read.read(value))
    case other                   => Failure(CannotConvert(classOf[Any], other))
  }
}
