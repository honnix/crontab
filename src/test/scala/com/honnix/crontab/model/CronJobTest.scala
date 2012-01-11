package com.honnix.crontab.model

import org.specs._
import org.specs.runner.{JUnit4, ConsoleRunner}
import org.specs.matcher._
import org.specs.specification._

class CronJobTestSpecsAsTest extends JUnit4(CronJobTestSpecs)
object CronJobTestSpecsRunner extends ConsoleRunner(CronJobTestSpecs)

object CronJobTestSpecs extends Specification {
  "CronJob" should {
    "Output its fields in certain format" in {
      val cronJob = new CronJob("25", "22", "13816082319", "what's up")
      cronJob.toString mustMatch """25 22 \* \* \* root    /bin/echo -e "@13816082319\\nwhat's up" > /var/lib/smsd/.+\.sms"""
    }

    "be able to list all cron jobs" in {
      val cronJobList = CronJob.findAll
      cronJobList.size must_== 2
      cronJobList(0).toString must include("message1")
      cronJobList(1).toString must include("message2")
    }

    "be able to validate itself, if minute is empty" in {
      val cronJob = new CronJob("", "22", "13816082319", "what's up")
      cronJob.validate must ==/(<error>not valid</error>)
    }

    "be able to validate itself, if hour is empty" in {
      val cronJob = new CronJob("25", "", "13816082319", "what's up")
      cronJob.validate must ==/(<error>not valid</error>)
    }

    "be able to validate itself, if mobile is not of length 11" in {
      val cronJob = new CronJob("25", "22", "1381608231", "what's up")
      cronJob.validate must ==/(<error>not valid</error>)
    }

    "be able to validate itself, if message is empty" in {
      val cronJob = new CronJob("25", "22", "13816082319", "")
      cronJob.validate must ==/(<error>not valid</error>)
    }

    "be able to validate itself, if everything is fine" in {
      val cronJob = new CronJob("25", "22", "13816082319", "what's up")
      cronJob.validate must_== Nil
    }

    "be able to save (new entry) and delete itself" in {
      val cronJob = new CronJob("00", "00", "13816082319", "what's up")
      cronJob.save

      var cronJobList = CronJob.findAll
      cronJobList.size must_== 3
      cronJobList(0).toString must include("what's up")
      cronJobList(1).toString must include("message1")
      cronJobList(2).toString must include("message2")

      cronJob.delete

      cronJobList = CronJob.findAll
      cronJobList.size must_== 2
      cronJobList(0).toString must include("message1")
      cronJobList(1).toString must include("message2")
    }

    "be able to save (modify existing entry) itself" in {
      val cronJob = new CronJob("55", "21", "13816082319", "message1")
      cronJob.oldToStringValue = cronJob.toString
      cronJob.message = "what's up"
      cronJob.save

      var cronJobList = CronJob.findAll
      cronJobList.size must_== 2
      cronJobList(0).toString must include("what's up")
      cronJobList(1).toString must include("message2")

      cronJob.oldToStringValue = cronJob.toString
      cronJob.message = "message1"
      cronJob.save

      cronJobList = CronJob.findAll
      cronJobList.size must_== 2
      cronJobList(0).toString must include("message1")
      cronJobList(1).toString must include("message2")
    }
  }
}
