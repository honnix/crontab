package com.honnix.crontab.snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.http._
import net.liftweb.util._
import S._
import js._
import js.JE._
import JsCmds._
import net.liftweb.common._
import SHtml._
import Helpers._

import com.honnix.crontab.model.CronJob

class Crontab {
  object currentCronJobVar extends RequestVar[CronJob] (
    CronJob("", "", "", "13816082319", "")
  )

  val currentCronJob = currentCronJobVar.is

  def list(xhtml: NodeSeq): NodeSeq = {
    def bindCronJob(cronJob: CronJob, xhtml: NodeSeq): NodeSeq = {
      def deleteCronJob: JsCmd = {
        cronJob.delete
        JsRaw("$('#" + cronJob.id + "').remove()")
      }

      <tr id={cronJob.id}>{
        bind("cronJob", chooseTemplate("crontab", "entries", xhtml),
             "minute" -> Text(cronJob.minute),
             "hour" -> Text(cronJob.hour),
             "mobile" -> Text(cronJob.mobile),
             "message" -> Text(cronJob.message),
             "actions" -> {link("/edit", () => currentCronJobVar(cronJob), Text("Edit")) ++ Text(" ") ++
                           a(Text("Delete")) {
                             deleteCronJob
                           }})
      }</tr>
    }

    val crontabHtml = CronJob.findAll.flatMap {
      bindCronJob(_, xhtml)
    }
    bind("crontab", xhtml, "entries" -> crontabHtml,
         "add" -> link("/edit", () => currentCronJobVar(currentCronJob), Text("Add")))
  }

  def edit(xhtml: NodeSeq): NodeSeq = {
    def save {
      currentCronJob.validate match {
        case Nil => {
          currentCronJob.save
          redirectTo("/crontab")
        }
        case x => error(x)
      }
    }

    val cronJob = currentCronJob

    bind("cronJob", xhtml,
         "id" -> hidden(() => {currentCronJobVar(cronJob); cronJob.oldToStringValue = cronJob.toString}),
         "minute" -> text(currentCronJob.minute, currentCronJob.minute = _),
         "hour" -> text(currentCronJob.hour, currentCronJob.hour = _),
         "mobile" -> text(currentCronJob.mobile, currentCronJob.mobile = _),
         "message" -> text(currentCronJob.message, currentCronJob.message = _),
         "save" -> submit("Save", save _))
  }
}
