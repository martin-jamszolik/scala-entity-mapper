
package org.viablespark.mapper.config

import scala.collection.mutable.ListBuffer

trait ConfigurationFactory[+F, C]:
  def getConfigurations(ctx: C): List[F]


object ConfigurationFactory extends ConfigurationFactory[MapperConfigTrait, ConfigContext] :
  private val list = new ListBuffer[MapperConfigTrait]

  def add(conf: MapperConfigTrait): Unit =
    list += conf

  override def getConfigurations(ctx: ConfigContext): List[MapperConfigTrait] =
    list.filter(conf => conf.sourceType == ctx.source.getClass && conf.targetType == ctx.target.getClass).toList
