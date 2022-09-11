package org.viablespark.mapper

import org.viablespark.mapper.config.*

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


trait EntityMapper:
  def map[T](source: AnyRef, target: T): T

class EntityMapperImpl(val configFactory: ConfigurationFactory[MapperConfigTrait, ConfigContext]) extends EntityMapper :
  def map[T](src: AnyRef, trg: T): T =
    val configurations = configFactory.getConfigurations(
      new ConfigContext {
        def source: AnyRef = src

        def target: AnyRef = trg.asInstanceOf[AnyRef]
      })

    val elContext = MapperContext(src, trg.asInstanceOf[AnyRef])

    for (config <- configurations)
      val info = config.getMappingInformation
      info.getData.foreach(item => mapData(item, elContext, src.getClass))

      if (info.getProcessors != null)
        info.getProcessors.foreach(processor => processor.process(src, trg.asInstanceOf[AnyRef]))
    trg


  private def mapData(item: MappingData, elContext: MapperContext, srcClass: Class[?]): Unit =
    try
      val sourceValue = elContext.source.getValue(item.source, classOf[AnyRef])

      //TODO: Create target if it is null

      // If the target is Array or Collection, loop through the collection
      if (item.collection.nonEmpty)
        mapCollection(item, elContext, sourceValue.asInstanceOf[Iterable[?]])
        return

      // Convert source value if a converter is specified
      val value = item.converter.getOrElse(NoOpsConverter.instance).convert(sourceValue)

      //Copy source value to target
      elContext.target.setValue(item.target, value.asInstanceOf[AnyRef])
    catch
      case e: Exception => println(s"Map data Error: ${e.getMessage}"); throw e;

  private def mapCollection(item: MappingData, elCtx: MapperContext, sourceValues: Iterable[?]): Unit =
    val list = new mutable.ListBuffer[AnyRef]
    for (sourceObject <- sourceValues)
      val targetObject = newInstance(item.objType, classOf[AnyRef])
      val indexContext = MapperContext(sourceObject.asInstanceOf[AnyRef], targetObject)
      item.collection.foreach(srcItem => {
        mapData(srcItem, indexContext, sourceObject.getClass)
      })
      list += targetObject
    elCtx.target.setValue(item.target, list)


  private def createTargetObject(item: MappingData, elCtx: MapperContext, value: AnyRef): Unit = {

  }

  private def newInstance[T](impl: String, cl: Class[T]): T =
    val clazz = Class.forName(impl)
    val result = clazz.getDeclaredConstructor().newInstance()
    cl.cast(result)
