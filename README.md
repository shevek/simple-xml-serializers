simple-xml-serializers
=====================

A set of extra serializers and plugins for
[SimpleXML](http://simple.sourceforge.net/).

Usage
-----

### Simple Usage

The simple case: PersisterFactory detects all ConverterFactory
and TransformFactory implementations using ServiceLoader and loads
them.  You may want to use Google's AutoFactory or openide-util's
ServiceProvider annotations to declare additional service-loadable
implementations.

```
Persister persister = PersisterFactory.newDefaultSerializer();
```

If you want to add custom Converters or Transforms without using
ServiceLoader, you can add them using addTransforms or addConverters.

```
PersisterFactory persisterFactory = new PersisterFactory();
persisterFactory.addTransforms(ServiceLoader.load(TransformFactory.class));	// Loads defaults
persisterFactory.addTransforms(...);	// Loads custom code
persisterFactory.addConverters(ServiceLoader.load(ConverterFactory.class));	// Loads defaults
persisterFactory.addConverters(...);	// Loads custom code
Persister persister = persisterFactory.newPersister();
```

### For Testing

Depend on the simple-xml-serializers-test module, and use
```
MyBean in = ...
MyBean out = PersisterTestUtils.testSerialization(in);	// Round-trip serialization and deserialization
assertEquals(in, out);	// Or more appropriate assertions for your system.
```

### With Spring Framework

If you are using Spring Framework: You can just import
a prebuilt configuration and then autowire the factory.
The prebuilt configuration uses @ComponentScan to detect all
ConverterFactory and TransformFactory implementations within
the org.anarres.simplexml.serializers namespace, and Spring will
additionally inject any other managed beans of type TransformFactory
or ConverterFactory:

```
@Import(SimpleXmlSerializersConfiguration.class)
...
@Autowired PersisterFactory persisterFactory;
```

### With Spring Framework for Remoting

You might also want to use Spring remoting using SimpleXML; the
necessary classes are in the simple-xml-serializers-spring-remote
module.

Documentation
-------------

The [JavaDoc API](http://shevek.github.io/simple-xml-serializers/docs/javadoc/)
is available.

Contributions
--------------

If you want simple-xml serialization for an open source library or
project, please raise a pull-request or an issue.

