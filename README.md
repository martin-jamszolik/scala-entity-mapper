# Scala Entity Mapper

Another solution to a complex object mapping written in Scala 3. 
This project uses an Expression Language Library to help with mapping.

**Entity mapper strengths:**

  1. Map deeply structured objects.
  1. Apply mapping from multiple definitions to map complex object.
  1. Load mapping configuration with custom implementation details
  1. Apply converters from source to target object.
  1. Works on domain objects externally.

**Entity mapper weaknesses:**
  1. All properties are mapped explicitly, as per configuration only. 
  1. Mapping configuration is one way only. If you need to reverse the mapping, you will need to provide another configuration.
  2. Data models have to have mutable properties. `case class` are fine as long as they have `var` properties.

## Roadmap

  1. Replace EL dependency with better native solution that is also null-safe. Avoid `@BeanProperty` decorator.
  1. Research if immutable objects can somehow be also mapped per configuration
  2. Add yaml Configuration option

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.
For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

### Learn

* Best place to start is with a test [MapperSuite](src/test/scala/MapperSuite.scala)
* Source of configuration [XML](src/test/resources/sample/beanA-to-beanB.xml)
* Source of configuration [YAML](src/test/resources/sample/beanA-to-beanB.yaml) **incomplete**

