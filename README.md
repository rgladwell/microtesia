# Microtesia [![Build Status](https://travis-ci.org/rgladwell/microtesia.svg)](https://travis-ci.org/rgladwell/microtesia) [![Codacy Badge](https://api.codacy.com/project/badge/1af50d2ea130484a918a44a5ce0d1ce0)](https://www.codacy.com/app/ricardo_3/microtesia) [ ![Download](https://api.bintray.com/packages/rgladwell/maven/microtesia/images/download.svg) ](https://bintray.com/rgladwell/maven/microtesia/_latestVersion)

[Microdata](http://www.w3.org/TR/microdata/) parsing library for
Scala.

To install add the following line to your SBT configuration:

```
libraryDependencies += "me.gladwell.microtesia" %% "microtesia" % "0.2"
```

To use simply put the Microtesia API in scope and call the `parse`
method as follows:

```scala
scala> import microtesia._
import microtesia._

scala> parseMicrodata("""<div
                          itemscope
                          itemtype="http://schema.org/Movie">
                            <h1 itemprop="name">Avatar</h1>
                          </div>""")
res0: Either[microtesia.InvalidMicrodata,microtesia.MicrodataDocument] = Right(MicrodataDocument(List(MicrodataItem(ArrayBuffer((name,MicrodataString(Avatar))),Some(http://schema.org/Movie),None))))
```

See the [API reference](http://rgladwell.github.io/microtesia/latest/api) for
more information.

Once the HTML has been parsed, you can extract microdata values using for-comprehensions:

```scala
scala> for {
         MicrodataItem(properties, _, _) <- res0.right.get.items
         MicrodataProperty("name", MicrodataString(string)) <- properties
       } yield string
res1: Seq[String] = List(Avatar)
```

See [MicrodataValueSpec.scala](https://github.com/rgladwell/microtesia/tree/master/src/test/scala/MicrodataValueSpec.scala) for more examples of microdata for-comprehensions.

## Readers

While you can use the for-comprehensions to write custom parsers, microtesia provides a `formats` API (based on [https://github.com/milessabin/shapeless](shapeless)) to automatically deserialise `MicrodataValue` instances into value types and case classes:

```scala
scala> import microtesia._
import microtesia._

scala> import formats._
import formats._

scala> case class Person(name: String, age: Int, adult: Boolean)
defined class Person

scala>  MicrodataItem(
          Seq(
            ("name", MicrodataString("hello")),
            ("age", MicrodataString("13")),
            ("adult", MicrodataString("true"))
          )
        ).convertTo[Person]
res0: Person = Person(hello,13,true)
```

## License

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this program.  If not, see
<http://www.gnu.org/licenses/>.

Copyright 2015-2016 [Ricardo Gladwell](http://gladwell.me).
