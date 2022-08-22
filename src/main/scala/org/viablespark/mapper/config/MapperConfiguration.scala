package org.viablespark.mapper.config

import scala.collection.mutable

trait MapperConfigTrait {
  val converters = new mutable.HashMap[String, Converter[_]]

  val sourceType:Class[_]
  val targetType:Class[_]

  def getMappingInformation: MappingInformation

  protected def getConverter(name: String): Option[Converter[_]] = {
    if (name == null || name.trim.isEmpty) {
      return None
    }
    Some(converters.getOrElseUpdate(name,
          newInstance[Converter[_]](name, classOf[Converter[_]]))
    )
  }

  protected def newInstance[T](impl: String, cl: Class[T]): T = {
    val clazz = Class.forName(impl)
    val result = clazz.getDeclaredConstructor().newInstance()
    result.asInstanceOf[T]
  }
}

trait ConfigContext {
  def source: AnyRef
  def target: AnyRef
}

