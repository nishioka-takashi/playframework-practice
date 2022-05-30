package models

import play.api.data.Form
import play.api.data.Forms._

object Member {
  val memberForm: Form[MemberForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "mail" -> nonEmptyText,
      "password" -> nonEmptyText
    )(MemberForm.apply)(MemberForm.unapply)
  }

  val memberLogin: Form[MemberLogin] = Form {
    mapping(
      "mail" -> nonEmptyText,
      "password" -> nonEmptyText
    )(MemberLogin.apply)(MemberLogin.unapply)
  }
}

case class Member(id: Int, name: String, mail: String, password: String)
case class MemberForm(name: String, mail: String, password: String)
case class MemberLogin(mail: String, password: String)
