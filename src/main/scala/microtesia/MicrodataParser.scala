// Copyright 2015-2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.io.Reader
import scala.util.Try

private trait MicrodataParser {

  def parse(input: Reader): Try[MicrodataDocument]

}
