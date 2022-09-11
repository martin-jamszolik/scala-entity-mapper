package org.viablespark.mapper

class MapperContext(src: AnyRef, trg: AnyRef):
  def source: MapperExpression = MapperExpression(src)

  def target: MapperExpression = MapperExpression(trg)