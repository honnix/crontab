package com.honnix.crontab.lib

import java.io.{PrintWriter, FileWriter}

import scala.io.Source

import com.honnix.crontab.model.CronJob

object CrontabFile {
  val file = if (!(System.getenv("CRONTAB_FILE") eq null)) System.getenv("CRONTAB_FILE") else "crontab_test"

  def read: List[Tuple4[String, String, String, String]] = {
    Source.fromFile(file).getLines.map(_.stripLineEnd).filter(_.matches(".+root +/bin/echo -e \"@.+")).map {x: String =>
      val minute = x.substring(0, 2)
      val hour = x.substring(3, 5)
      val firstDQuote = x.indexOf("\"")
      val secondDQuote = x.lastIndexOf("\"")
      val mobileMessage = x.substring(firstDQuote + 1, secondDQuote).split("\\\\n")
      val mobile = mobileMessage(0).substring(1)
      val message = mobileMessage(1)
      (minute, hour, mobile, message)
    }.toList
  }

  def save(cronJobList: List[CronJob]) {
    val otherLines = Source.fromFile(file).getLines.map(_.stripLineEnd).filter(!_.matches(".+root +/bin/echo -e \"@.+")).toList

    val pw = new PrintWriter(new FileWriter(file))
    otherLines.foreach(pw.println)
    cronJobList.foreach(x => pw.println(x.toString))
    pw.close
  }
}
