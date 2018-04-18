// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import microtesia._
import shapeless._
import labelled._
import scala.util.{Failure, Success, Try}

/**
 * Implicit microdata formats for case classes using [shapeless].
 * This needs to be in scope to support automatic conversion
 * of case classes.
 * It is include as part of the [[formats]] package object.
 */
trait ShapelessFormats {

  /**
   * Implicit [[MicrodataPropertyFormat]] to convert simple microdata properties.
   */
  implicit def simplePropertyFormat[T](implicit format: MicrodataFormat[T]) = new MicrodataPropertyFormat[T] {
    override def read(properties: Seq[MicrodataValue]): Try[T] = format.read(properties.head)
  }

  /**
   * Implicit [[MicrodataFormat]] to convert [[MicrodataValue]]s to empty HLists.
   */
  implicit object HNilFormat extends MicrodataFormat[HNil] {
    override def read(microdata: MicrodataValue) = Success(HNil)
  }

  /**
   * Implicit [[MicrodataFormat]] to convert [[MicrodataValue]]s to HLists.
   */
  implicit def hlistFormat[Key <: Symbol, Value, Remaining <: HList](
    implicit
    key: Witness.Aux[Key],
    mfh: Lazy[MicrodataPropertyFormat[Value]],
    mft: Lazy[MicrodataFormat[Remaining]]
  ) = {
    new MicrodataFormat[FieldType[Key, Value] :: Remaining] {

      override def read(microdata: MicrodataValue) = microdata match {
        case item: MicrodataItem => read(item)
        case other               => Failure(CannotConvert(classOf[FieldType[Key, Value] :: Remaining], other))
      }

      private def read(item: MicrodataItem) = {
        val properties = (item \ key.value.name).results
        for {
          head <- mfh.value.read(properties)
          tail <- mft.value.read(item)
        } yield (field[Key](head) :: tail)
      }
    }

  }

  /**
   * Implicit [[MicrodataFormat]] to convert [[MicrodataValue]]s to case classes.
   */
  implicit def caseClassFormat[T, Repr](
    implicit
    generic: LabelledGeneric.Aux[T, Repr],
    format: Lazy[MicrodataFormat[Repr]]
  ) =
    new MicrodataFormat[T] {
      override def read(microdata: MicrodataValue): Try[T] = {
        for(hlist <- format.value.read(microdata)) yield generic.from(hlist)
      }
    }

}
