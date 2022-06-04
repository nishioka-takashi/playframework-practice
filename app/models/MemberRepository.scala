package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class MemberRepository @Inject()
  (dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext) {

    private val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    private class MembersTable(tag: Tag)
      extends Table[Member](tag, "members") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def name = column[String]("name")
        def mail = column[String]("mail")
        def password = column[String]("password")

        def * = (id, name, mail, password) <>
          ((Member.apply _).tupled, Member.unapply)
    }

    private val members = TableQuery[MembersTable]

    def list(): Future[Seq[Member]] = db.run {
      members.result
    }

    def create(name: String, mail: String, password: String): Future[Int] =
      db.run {
        members += Member(0, name, mail, password)
      }
    
    def authenticate(email: String, password: String): Future[Option[Member]] = db.run {
      members.filter(m => (m.mail === email) && (m.password === password)).result.headOption
    }
  }