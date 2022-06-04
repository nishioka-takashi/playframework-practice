package controllers

import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MyPageController @Inject()(
    repository: MemberRepository,
    cc: MessagesControllerComponents
  )(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc) {

    def index() = Action.async { implicit request =>
      val mail = request.session.get("mail")
      val pass = request.session.get("password")

      (mail, pass) match {
        case (Some(x), Some(y)) => 
          repository.authenticate(x, y).map { member =>
            member match {
              case Some(m: Member) => Ok(views.html.mypage(m))
              case _ => Ok(views.html.login(Member.memberLogin))
            }
          }
        case _ => Future{Ok(views.html.login(Member.memberLogin))}
      }
    }

    def login() = Action.async { implicit request =>
      Member.memberLogin.bindFromRequest.fold(
        errorForm => {
          Future{Ok(views.html.login(Member.memberLogin))}
        },
        member => {
          repository.authenticate(member.mail, member.password).map { member =>
            member match {
              case Some(m: Member) =>
                Ok(views.html.mypage(m)).withSession(
                  "mail" -> m.mail,
                  "password" -> m.password
                )
              case _ => Ok(views.html.login(Member.memberLogin))
            }
          }
        }
      )
    }

    def logout() = Action { implicit request =>
      Ok(views.html.login(Member.memberLogin)).withNewSession
    }
  }