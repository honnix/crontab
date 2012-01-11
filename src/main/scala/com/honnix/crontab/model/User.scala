package com.honnix.crontab.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

class User extends MegaProtoUser[User] {
  def getSingleton = User
}

object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			                     <lift:bind /></lift:surround>)

  override def signupFields = firstName :: lastName ::email :: password :: Nil

  override def skipEmailValidation = true
}

