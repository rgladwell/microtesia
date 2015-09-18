// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification

object EitherSequenceSpec extends Specification {
  
  "Sequence of Either should" >> {

    "sequence a successful Seq[Either] into Either[Seq]" >> {
      Seq(Right(1), Right(2), Right(3)).sequence must_== Right(Seq(1,2,3))
    }

    "sequence a Seq[Either] containing a failure into a failure" >> {
      Seq(Right(1), Left("failure"), Right(3)).sequence must_== Left("failure")
    }

    "sequence a Seq[Either] containing multiple failures into the last failure" >> {
      Seq(Left("first"), Right(1), Left("last")).sequence must_== Left("last")
    }

    "traverse a successful Seq[Either[Seq[B]] into Either[Seq[B]]" >> {
      val seq: Seq[Either[String,Seq[Int]]] = Seq(Right(Seq(1,2)), Right(Seq(3)), Right(Seq(4,5)))

      seq.traverse(Nil: Seq[Int]){ _ ++ _ } must_== Right(Seq(1,2,3,4,5))
    }

  }

}
