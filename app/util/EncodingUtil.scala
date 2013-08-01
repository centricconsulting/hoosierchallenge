package util

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils

object EncodingUtil {
  def compressText(text:String) : Option[String] = {
    val str = new ByteArrayOutputStream(text.length)
    val gzip = new GZIPOutputStream(str)

    try {
      gzip.write(text.getBytes())
      gzip.close()

      Some(new String(Base64.encodeBase64(str.toByteArray)))
    }
    catch {
      case _ : Throwable => None
    }
    finally {
      Array(gzip, str).foreach(IOUtils.closeQuietly(_))
    }
  }
  def inflateText(compressed:String) : Option[String] = {
    val str = new ByteArrayInputStream(Base64.decodeBase64(compressed.getBytes))
    val gunzip = new GZIPInputStream(str)
    try {
      Some(IOUtils.toString(gunzip))
    }
    catch {
      case _ : Throwable => None
    }
    finally {
      Array(gunzip, str).foreach(IOUtils.closeQuietly(_))
    }
  }
}
