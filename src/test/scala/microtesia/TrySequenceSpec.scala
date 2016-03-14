// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import org.specs2.mutable.Specification
import scala.util.{Failure, Success, Try}

object TrySequenceSpec extends Specification {
  
  "Sequence of Try should" >> {

    "sequence a successful Seq[Try] into Try[Seq]" >> {
      Seq(Success(1), Success(2), Success(3)).sequence must_== Success(Seq(1,2,3))
    }

    "sequence a Seq[Try] containing a failure into a failure" >> {
      val e = new Exception
      Seq(Success(1), Failure(e), Success(3)).sequence must_== Failure(e)
    }

    "sequence a Seq[Try] containing multiple failures into the last failure" >> {
      val first = new Exception
      val last = new Exception
      Seq(Failure(first), Success(1), Failure(last)).sequence must_== Failure(last)
    }

    "traverse a successful Seq[Try[Seq[B]] into Try[Seq[B]]" >> {
      val seq: Seq[Try[Seq[Int]]] = Seq(Success(Seq(1,2)), Success(Seq(3)), Success(Seq(4,5)))

      seq.traverse(Nil: Seq[Int]){ _ ++ _ } must_== Success(Seq(1,2,3,4,5))
    }

  }

}
