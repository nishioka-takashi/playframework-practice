package utils

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Weather(publishingOffice: String, reportDatetime: String, targetArea: String, text: String)
object Weather {
  implicit val weatherReads: Reads[Weather] = (
    (JsPath \ "publishingOffice").read[String] and
    (JsPath \ "reportDatetime").read[String] and
    (JsPath \ "targetArea").read[String] and
    (JsPath \ "text").read[String]
  )(Weather.apply _)
}
