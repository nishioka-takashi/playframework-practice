package controllers

import javax.inject._

import forms._
import utils._
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._

@Singleton
class ApiController @Inject()(cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) {

  val apiForm = Form(
    mapping(
      "area" -> nonEmptyText
    )(ApiForm.apply)(ApiForm.unapply)
  )

  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.api(
      apiForm
    ))
  }

  def call() = Action { implicit request: MessagesRequest[AnyContent] =>
    val form = apiForm.bindFromRequest
    form.fold(
      errorForm => {
        BadRequest(views.html.api(errorForm))
      },
      area => {
        val r = requests.get("https://www.jma.go.jp/bosai/forecast/data/overview_forecast/" + area.area + ".json")
        val json: JsValue = Json.parse(r.text)
        val weatherResult: JsResult[Weather] = json.validate[Weather]
        println(weatherResult)
        weatherResult match {
          case s: JsSuccess[Weather] => Ok(views.html.api(form, s.get))
          case e: JsError => Ok(views.html.api(form))
        }
      }
    )
  }
}