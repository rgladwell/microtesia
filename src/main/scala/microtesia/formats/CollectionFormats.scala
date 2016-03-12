// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import microtesia.MicrodataValue

/**
 * Implicit microdata formats for common collection types, including [[Seq]] and [[Option]].
 * This needs to be in scope to support automatic conversion of these types. It is include as
 * part of the [[formats]] package object.
 */
trait CollectionFormats {

  private def sequence[T](s: Seq[Converted[T]]): Converted[Seq[T]] =
    s.find{ _.isLeft } match {
      case Some(Left(error)) => Left(error)
      case _ => Right(s.map { _.right.get })
    }

  private def toSeq[T](properties: Seq[MicrodataValue])(implicit format: MicrodataFormat[T]) =
    sequence(properties.map(format.read(_)))

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata properties to [[scala.Seq]].
   */
  implicit def seqFormat[T](implicit format: MicrodataFormat[T]) = new MicrodataPropertyFormat[Seq[T]] {
    override def read(properties: Seq[MicrodataValue]): Converted[Seq[T]] = toSeq(properties)
  }

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata properties to [[scala.Set]].
   */
  implicit def setFormat[T](implicit format: MicrodataFormat[T]) = new MicrodataPropertyFormat[Set[T]] {
    override def read(properties: Seq[MicrodataValue]): Converted[Set[T]] = toSeq[T](properties).right.map{ _.toSet }
  }

  /**
   * Implicit [[MicrodataFormat]] to convert multiple microdata properties to [[scala.List]].
   */
  implicit def listFormat[T](implicit format: MicrodataFormat[T]) = new MicrodataPropertyFormat[List[T]] {
    override def read(properties: Seq[MicrodataValue]): Converted[List[T]] = toSeq[T](properties).right.map{ _.toList }
  }

  /**
   * Implicit [[MicrodataFormat]] to convert optional microdata properties to [[scala.Option]].
   */
  implicit def optionFormat[T](implicit format: MicrodataFormat[T]) = new MicrodataPropertyFormat[Option[T]] {
    override def read(properties: Seq[MicrodataValue]): Converted[Option[T]] = toSeq[T](properties).right.map{ _.headOption }
  }

}
