// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import scala.util.{Success, Try}

private class TrySequence[A](seq: Seq[Try[A]]) {

  def sequence: Try[Seq[A]] = traverse[Seq[A]](Nil)( _ +: _)

  def traverse[B](init: B)(bop : (A, B) => B): Try[B] =
    seq.foldRight(Success(init): Try[B]) {
      (e, acc) => for (xs <- acc; x <- e) yield bop(x, xs)
    }

}
