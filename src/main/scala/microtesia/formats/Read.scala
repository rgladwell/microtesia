// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

private trait Read[T] {
  def read(string: String): T
}
