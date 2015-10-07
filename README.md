# Microtesia [![Build Status](https://travis-ci.org/rgladwell/microtesia.svg)](https://travis-ci.org/rgladwell/microtesia)

[Microdata](http://www.w3.org/TR/microdata/) parsing library for
Scala.

To use simply put the Microtesia API in scope and call the `parse`
method as follows:

```scala
scala> import microtesia._
import microtesia._

scala> parse("""<div itemscope itemtype="http://schema.org/Movie"><h1 itemprop="name">Avatar</h1></div>""")
res0: Either[microtesia.InvalidMicrodata[scala.xml.Node],microtesia.MicrodataDocument] = Right(MicrodataDocument(List(MicrodataItem(Some(http://schema.org/Movie),Map(name -> List(MicrodataString(Avatar))),None))))
```

See the [API reference](http://rgladwell.github.io/microtesia) for
more information.

## License

This program is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this program.  If not, see
<http://www.gnu.org/licenses/>.

Copyright 2015 [Ricardo Gladwell](http://gladwell.me).
