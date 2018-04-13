// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

import microtesia.MicrodataValue

case class CannotConvert(c: Class[_], value: MicrodataValue) extends Exception(s"cannot convert $value to $c")
