// Copyright 2016 Ricardo Gladwell.
// Licensed under the GNU Lesser General Public License.
// See the README.md file for more information.

package microtesia.formats

private object ValueReads {

  implicit object readString extends Read[String] {
    override def read(string: String) = string
  }

  implicit object readInt extends Read[Int] {
    override def read(string: String) = string.toInt
  }

  implicit object readDouble extends Read[Double] {
    override def read(string: String) = string.toDouble
  }

  implicit object readFloat extends Read[Float] {
    override def read(string: String) = string.toFloat
  }

  implicit object readLong extends Read[Long] {
    override def read(string: String) = string.toLong
  }

  implicit object readShort extends Read[Short] {
    override def read(string: String) = string.toShort
  }

  implicit object readBoolean extends Read[Boolean] {
    override def read(string: String) = string.toBoolean
  }

}
