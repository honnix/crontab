package com.honnix.crontab.snippet

import org.specs._
import org.specs.runner.{JUnit4, ConsoleRunner}
import org.specs.matcher._
import org.specs.specification._
import net.liftweb._
import http._
import net.liftweb.util._
import net.liftweb.common._
import Helpers._
import lib._

class CrontabTestSpecsAsTest extends JUnit4(CrontabTestSpecs)
object CrontabTestSpecsRunner extends ConsoleRunner(CrontabTestSpecs)

object CrontabTestSpecs extends Specification {
  val session = new LiftSession("", randomString(20), Empty)

  "Crontab Snippet" should {
    doAroundExpectations(S.initIfUninitted(session)(_))

    "list all cron jobs defined by me (except those shipped with system)" in {
      val crontab = new Crontab
      val str = crontab.list(<div><crontab:entries>
                             <cronJob:minute />
                             <cronJob:hour />
                             <cronJob:mobile />
                             <cronJob:message />
                             </crontab:entries>
                             <crontab:add /></div>).toString

      str.indexOf("message1") must be >= 0
      str.indexOf("message2") must be >= 0
      str.indexOf("Add") must be >= 0
      str.indexOf("run-crons") must be < 0
    }

    "be able to edit a certain cron job" in {
      val crontab = new Crontab
      val str = crontab.edit(<div>
                             <cronJob:id />
                             <cronJob:minute />
                             <cronJob:hour />
                             <cronJob:mobile />
                             <cronJob:message />
                             <cronJob:save />
                             </div>).toString

      str.indexOf("13816082319") must be >= 0
    }
  }
}
