simple-xml-serializers
=====================

A set of extra serializers and plugins for
[SimpleXML](http://simple.sourceforge.net/).

Usage
-----

```
PersisterFactory factory = new PersisterFactory();
factory.addTransforms(ServiceLoader.load(TransformFactory.class));
factory.addConverters(ServiceLoader.load(ConverterFactory.class));
Persister persister = factory.newPersister();
```

Then continue as normal.

