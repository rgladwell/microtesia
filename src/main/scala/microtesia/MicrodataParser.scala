// Copyright 2015 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia

import java.io.Reader

private trait MicrodataParser[N] {

  def parse(input: Reader): Parsed[MicrodataDocument, N]

}
