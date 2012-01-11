package com.honnix.crontab.model

import java.util.{Calendar, Random}

import com.honnix.crontab.lib.CrontabFile

case class CronJob(var id: String, var minute: String, var hour: String, var mobile: String, var message: String) {
  var oldToStringValue: String = ""

  def this(minute: String, hour: String, mobile: String, message: String) = this("", minute, hour, mobile, message)

  private def compare(cronJob: CronJob) = {
    val index = oldToStringValue.indexOf("/var/lib/smsd/")
    if (index < 0) false else
      CronJob.compareIgnoreFileName(oldToStringValue, cronJob.toString)
  }

  override def toString = minute + " " + hour + " * * * root    " + 
    "/bin/echo -e \"@" + mobile + "\\n" + message + "\" > /var/lib/smsd/" +
    Calendar.getInstance.getTimeInMillis + (new Random).nextInt(Integer.MAX_VALUE) + ".sms"

  def save {
    CrontabFile.save(this :: CronJob.findAll.filter(x => !compare(x) & !CronJob.compareIgnoreFileName(x.toString, toString)).toList)
  }

  def delete {
    CrontabFile.save(CronJob.findAll.filter(x => !CronJob.compareIgnoreFileName(x.toString, toString)).toList)
  }

  def validate = 
    if (minute == "" || hour == "" || mobile.length != 11 || message == "")
      <error>not valid</error>
    else Nil
}

object CronJob {
  def findAll: List[CronJob] = CrontabFile.read.map {x =>
    new CronJob((new Random).nextInt(Integer.MAX_VALUE).toString, x._1, x._2, x._3, x._4)
  }

  def compareIgnoreFileName(op1: String, op2: String) =
    op1.substring(0, op1.indexOf("/var/lib/smsd")) == op2.substring(0, op2.indexOf("/var/lib/smsd"))
}
