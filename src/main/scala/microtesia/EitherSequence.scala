// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

private class EitherSequence[A, B](seq: Seq[Either[A,B]]) {

  def sequence: Either[A, Seq[B]] = traverse[Seq[B]](Nil)( _ +: _)

  def traverse[C](init: C)(bop : (B, C) => C): Either[A, C] =
    seq.foldRight(Right(init): Either[A, C]) {
      (e, acc) => for (xs <- acc.right; x <- e.right) yield bop(x, xs)
    }

}
