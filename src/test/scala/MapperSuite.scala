

import org.viablespark.mapper.*
import org.viablespark.mapper.config.*

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class MapperSuite extends munit.FunSuite :
  test("should map BeanA to BeanB correctly") {
    val entityMapper = getEntityMapper
    val a = BeanA("martin", "some description", 28, Phone("123452032", "Home"))
    a.groups.append(Group("group1", "rank1"))
    a.groups.append(Group("group2", "rank2"))

    val b = BeanB("", "", 0, "")


    var result = entityMapper.map(a, b)
    assert(result != null)
    assertEquals("martin", result.title)
    assertEquals(28, result.years)
    a.desc = "second time desc"
    a.age = 31

    result = entityMapper.map(a, b)

    assertEquals("martin", result.title)
    assertEquals(31, result.years)
    assertEquals("second time desc", result.description)
    assertEquals(b.phoneNumber, a.phone.number)
    assertEquals(a.groups.size, b.entityGroups.size)

    for (index <- a.groups.indices)
      assertEquals(a.groups.lift(index).get.name, b.entityGroups.lift(index).get.groupName)
      assertEquals(a.groups.lift(index).get.rank, b.entityGroups.lift(index).get.rank)
  }


  private def getEntityMapper: EntityMapper =
    ConfigurationFactory.add(
      SimpleXmlMapperConfig("/sample/beanA-to-beanB.xml", classOf[BeanA], classOf[BeanB]),
    )
    EntityMapperImpl(ConfigurationFactory)


case class BeanA(@BeanProperty var name: String,
                 @BeanProperty var desc: String,
                 @BeanProperty var age: Int,
                 @BeanProperty var phone: Phone):
  @BeanProperty
  var groups: mutable.Buffer[Group] = ListBuffer[Group]()


case class BeanB(@BeanProperty var title: String,
                 @BeanProperty var description: String,
                 @BeanProperty var years: Int,
                 @BeanProperty var phoneNumber: String):

  @BeanProperty
  var entityGroups: mutable.Buffer[GroupEntity] = ListBuffer[GroupEntity]()

case class Group(@BeanProperty var name: String = "", @BeanProperty var rank: String = ""):
  def this() = this("", "")


case class GroupEntity(@BeanProperty var groupName: String = "", @BeanProperty var rank: String = ""):
  def this() = this("", "")

case class Phone(@BeanProperty var number: String,
                 @BeanProperty var where: String)


class ReportResultPostProcessor extends PostProcessor :
  override def process(source: AnyRef, target: AnyRef): Unit =
    println(s"Post Process Invoked. Processing source: $source, target: $target")



