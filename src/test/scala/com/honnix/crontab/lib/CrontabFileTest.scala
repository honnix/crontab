package com.honnix.crontab.lib

import org.specs._
import org.specs.runner.{JUnit4, ConsoleRunner}
import org.specs.matcher._
import org.specs.specification._

import com.honnix.crontab.model.CronJob

class CrontabFileTestSpecsAsTest extends JUnit4(CrontabFileTestSpecs)
object CrontabFileTestSpecsRunner extends ConsoleRunner(CrontabFileTestSpecs)

object CrontabFileTestSpecs extends Specification {
  "CrontabFile" should {
    "be able to read all cron jobs from crontab file (except those shipped with system)" in {
      val cronJobList = CrontabFile.read
      cronJobList.size must_== 2
      cronJobList(0)._1 must_== "55"
      cronJobList(0)._2 must_== "21"
      cronJobList(0)._3 must_== "13816082319"
      cronJobList(0)._4 must_== "message1"
      cronJobList(1)._1 must_== "00"
      cronJobList(1)._2 must_== "14"
      cronJobList(1)._3 must_== "13816082319"
      cronJobList(1)._4 must_== "message2"
    }

    "be able to save a cron job list" in {
      CrontabFile.save(List(CronJob("11", "22", "13816082319", "what's up"),
                            CronJob("33", "44", "13816082319", "hi, dude")))

      var cronJobList = CrontabFile.read
      cronJobList.size must_== 2
      cronJobList(0)._1 must_== "11"
      cronJobList(0)._2 must_== "22"
      cronJobList(0)._3 must_== "13816082319"
      cronJobList(0)._4 must_== "what's up"
      cronJobList(1)._1 must_== "33"
      cronJobList(1)._2 must_== "44"
      cronJobList(1)._3 must_== "13816082319"
      cronJobList(1)._4 must_== "hi, dude"

      CrontabFile.save(List(CronJob("55", "21", "13816082319", "message1"),
                            CronJob("00", "14", "13816082319", "message2")))

      cronJobList = CrontabFile.read
      cronJobList.size must_== 2
      cronJobList(0)._1 must_== "55"
      cronJobList(0)._2 must_== "21"
      cronJobList(0)._3 must_== "13816082319"
      cronJobList(0)._4 must_== "message1"
      cronJobList(1)._1 must_== "00"
      cronJobList(1)._2 must_== "14"
      cronJobList(1)._3 must_== "13816082319"
      cronJobList(1)._4 must_== "message2"
    }
  }
}
