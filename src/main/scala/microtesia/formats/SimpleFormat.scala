// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import microtesia._

private class SimpleFormat[V](implicit read: Read[V]) extends MicrodataFormat[V] {
  override def read(microdata: MicrodataValue): Converted[V] = microdata match {
    case MicrodataString(value)  => Right(read.read(value))
    case other                   => Left(CannotConvert(classOf[Any], other))
  }
}
