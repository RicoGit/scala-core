package root.core.serialisation

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import scala.util.control.NonFatal


object Serialisation extends App {

  val fileName = "tempData.ser"

  val obj = Object
  val map = Map("key" -> obj)

  // deserialize object with not serializable filed

  serializeObject(obj)
  val desObj = deserializeObj
  assert(desObj.get eq obj, "deserialized obj should be the same")


//  serializeObject(map)
//  val desMap = deserializeObj
//  assert(desMap.get.asInstanceOf[map.type]("key") == map, "deserialized map should be the same")

  private def deserializeObj: Option[AnyRef] = {
    try {
      val fis = new FileInputStream(fileName)
      val ois = new ObjectInputStream(fis)
      Some(ois.readObject())
    } catch { case NonFatal(e) =>
        println(s"Deserialization fail $e")
        None
    }
  }

  private def serializeObject(obj: AnyRef) {
    try {
      val fos = new FileOutputStream(fileName)
      val oos = new ObjectOutputStream(fos)
      oos.writeObject(obj)
      oos.close()
    } catch { case NonFatal(e) =>
        println(s"Serialization fail $e")
    }
  }

}

object Object extends Serializable {
  @transient private val logger = new Logger()
}

class Logger {
  def log() = println("log")
}
