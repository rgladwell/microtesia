// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import microtesia._
import java.net.URI

/**
 * Implicit microdata formats for rich data types, including [[URI]]. This needs
 * to be in scope to support automatic conversion of these types. It is include
 * as part of the [[formats]] package object.
 */
trait RichTypeFormats {

  /**
   * Implicit [[MicrodataFormat]] to convert [[MicrodataValue]]s to [[URI]].
   */
  implicit object LinkFormat extends MicrodataFormat[URI] {
    override def read(microdata: MicrodataValue): Converted[URI] = microdata match {
      case MicrodataLink(value)  => Right(value)
      case other                 => Left(CannotConvert(classOf[URI], other))
    }
  }

}
