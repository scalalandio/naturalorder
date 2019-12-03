package io.scalaland.naturalorder

/** Quick conversion of
 *
 * https://github.com/paour/natorder/blob/master/NaturalOrderComparator.java
 *
 * into Scala - what I have the time to rewrite I rewritten, otherwise I simply converted to Java.
 */
// scalastyle:off
class NaturalLanguageOrdering extends Ordering[String] {

  def compare(a: String, b: String): Int = {
    var ia  = 0
    var ib  = 0
    var nza = 0
    var nzb = 0
    var ca  = 0
    var cb  = 0
    while (true) { // Only count the number of zeroes leading the last number compared
      nza = 0
      nzb = 0
      ca  = charAt(a, ia)
      cb  = charAt(b, ib)
      // skip over leading spaces or zeros
      while ({ Character.isSpaceChar(ca) || ca == '0' }) {
        if (ca == '0') nza += 1
        else { // Only count consecutive zeroes
          nza = 0
        }
        ca = charAt(a, { ia += 1; ia })
      }
      while ({ Character.isSpaceChar(cb) || cb == '0' }) {
        if (cb == '0') nzb += 1 else nzb = 0
        cb = charAt(b, { ib += 1; ib })
      }
      // Process run of digits
      if (Character.isDigit(ca) && Character.isDigit(cb)) {
        val bias = compareRight(a.substring(ia), b.substring(ib))
        if (bias != 0) return bias
      }
      if (ca == 0 && cb == 0) { // The strings compare the same. Perhaps the caller
        // will want to call strcmp to break the tie.
        return nza - nzb
      }
      if (ca < cb) return -1
      if (ca > cb) return +1
      ia += 1
      ib += 1
    }
    ???
  }

  private def compareRight(a: String, b: String): Int = {
    var bias = 0
    var ia   = 0
    var ib   = 0
    // The longest run of digits wins. That aside, the greatest
    // value wins, but we can't know that it will until we've scanned
    // both numbers to know that they have the same magnitude, so we
    // remember it in BIAS.

    while ({ true }) {
      val ca = charAt(a, ia)
      val cb = charAt(b, ib)
      if (!Character.isDigit(ca) && !Character.isDigit(cb)) return bias
      if (!Character.isDigit(ca)) return -1
      if (!Character.isDigit(cb)) return +1
      if (ca == 0 && cb == 0) return bias
      if (bias == 0) if (ca < cb) bias = -1 else if (ca > cb) bias = +1

      { ia += 1; ia - 1 }
      { ib += 1; ib - 1 }
    }
    ???
  }

  private def charAt(s: String, i: Int): Int = if (i >= s.length) 0 else s.charAt(i).toInt
}
