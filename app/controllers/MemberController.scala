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

/**
 * 
 */
@Singleton
class MemberController @Inject()(
    repository: MemberRepository,
    cc: MessagesControllerComponents
  )(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc) {

    def index() = Action.async { implicit request =>
      repository.list().map { members =>
        Ok(views.html.member(
          Member.memberForm,
          members
        ))
      }
    }

    def create() = Action.async { implicit request =>
      Member.memberForm.bindFromRequest.fold(
        errorForm => {
          repository.list().map { members =>
            Ok(views.html.member(errorForm, members))
          }
        },
        member => {
          repository.create(member.name, member.mail, member.password).map { _ =>
            Redirect(routes.MemberController.index)
          }
        }
      )
    }
  }
