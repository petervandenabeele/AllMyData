/**
  * Created by peter_v on 27/03/16.
  */

package cli

object Util {

  def getFileName(args: Array[String]) = args match {
    case Array(f) => f
    case _ => throw new RuntimeException("provide a filename to read from")
  }

  def getFullFilename(filename: String, dir: String = "data") = {
    val homeDir = System.getProperty("user.home")
    homeDir + s"/pp/facts/$dir/" + filename
  }

}
