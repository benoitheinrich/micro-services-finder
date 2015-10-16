# README

This project aims to allow developer to find candidates for microservices.

In order ot work, the tool needs the project to follow some conventions:
- The project must be a multi module maven project
- The project should define `api` directories composed of **API** modules
- The project should defin `impl` directories composed of **implementation** modules
  (and possibly mock modules)

# How it works

The tool will analyse the maven dependencies of each of the implementation modules,
and it'll try to find the ones that have the least dependencies on other APIs and
Implementations.

As an implementation module never depends on some other implementation module, but only on
other API modules, the tool will then see what implementation modules could be used in
order to implement that API.

This will then virtually link implementation modules together through corresponding APIs,
and it'll create a new dependency graph based on the dependencies defined by those
implementations.

In the end, each implementation will then know the complete list of other implementations
that would be required if we wanted to create a standalone microservice.

Each group of standalone implementation modules will be stored as dotty files so they can
be used later on to generate nice images.

In fact the tool allows you to generate different output types using the `-f` option.

# Command line options

The tool comes with a simple CLI executable that accepts many different options.

The main executable is started using the following command:

    $ java -jar target/scala-2.11/micro-services-finder.jar

You should really play with the `--help` to see all it can do
for you.

Here is an extract of the help command:

```
Usage: micro-services-finder [options]

  -c | --clear
        If specified indicates the tool should clear all caches prior running.

  -f <value> | --format <value>
        If specified indicates the output format to be used by the tool. Format are passed
        directly to the graphviz command to use and accepts any value as supported by your
        graphviz installation. By default it generates a .dot file format.

  -o <value> | --output <value>
        If specified indicates the place where the generated files will be stored. By
        default it generates them in the current directory. The -o option is also used to
        indicate where the index files will be generated.

  -s <value> | --source <value>
        If specified indicates where to analyse source code from. By default it analyses
        the source code from the current directory. The -s option is also used to indicate
        where to locate the configuration file to be used by the tool.

  -v | --verbose
        If specified indicates the tool needs to generate useful information to the
        reader to understand what's happening.

  --help
        prints this usage text


```

**Examples:**

This will parse the files located in the /path/to/mycode folder, and it'll generate
a list of png files in the current directory.

    $ java -jar target/scala-2.11/micro-services-finder.jar -s /path/to/mycode -f png


# Under the hoods

In order to know which module implements a given API, we first need to know what is
contained in a given API module.

An API module is in general composed of some **POJOs** and some **Service Definitions**.

**POJOs** are in general simple Java classes, while **Service Definitions** are in general
represented by interfaces.

*The tool will only use java files defined in the main sources of the module, and it'll ignore test sources.*

## Indexing Service Definitions
The first thing the tool does is to index all API modules types hierarchy.

This first step is very important so it indicates the **capabilities** of an API.

Those **capabilities** are based on the list of **Service Definitions** defined in that
API module (the list of interfaces).

Also, in some cases a **Service Definition** can be composed of other services, and for
this it will then **extend** another **Service Definition** which could be defined in
the same API module, or in some other API modules.

On the other hands, some **POJOs** might also **extend** other **POJOs** or abstract
**POJOs**, but those cases should be ignored.

*Cases where an interface implements a standard Java type (ie. `extends Comparable`)
should be ignored as well*.

The indexing will then result in a list of **Service Definitions** per API module, and each
**Service Definition** will indicate if it extends other **Service Definitions**, and this
will create a graph of physical dependencies.

## Indexing Service Implementations
Similar to the indexing of the API types, the implementation modules will need to be indexed
as well.

The indexing of the implementation will be one only for classes which extends a
**Service Definition**.

Other classes will be ignored as they can only be used by the internals of the implementation
module itself.

The way the tool indexes the implementation modules is that it'll check all the classes that
implement an interface, and then it'll check if that interface is considered as a
**Service Definition** based on the previous step.

If the class is considered as an implementation of a **Service Definition**, then that
class will be indexed, and it'll indicate which **Service Definition** is implemented and
which module contains that implementation.

This will be referred as a **Service Implementation**.

## Multiple Service Implementations
As a **Service Definitions** can be implemented by many **Service Implementations**, the
tool needs to make a choice about which one to use if you were to create a standalone
microservice.

The first thing to do is to check the type of implementation and see if maybe one of the
implementation is a **Mock Service**.

A **Mock Service** is similar to a real implementation module, except that in general the
name of the class will start by the word `Mock`.

**Mock Services** will have to be ignored by the tool as they can't be delivered as part of
a microservice.

After ignoring **Mock Services**, if we still have more than one match, then the tool will
accept a configuration file to indicate which **Service Implementations** to use.

*Note that many **Service Implementations** can be used if needed, and by default, all
the **Service Implementations** will be used.*

## Persisting indexes
The indexes created by the tools will be persisted so they can be reused without having to
parse the code base again and again.

The indexes will be persisted in a simple json file describing the **Service Definitions**
and the **Service Implementations**.

The **Service Definitions** will be stored in a `service-definitions.json` file in the
output directory (based on `o` option), and the **Service Implementations** will be stored in the
`service-implementations.json` file (still in output directory).

If one of the file is missing, then it'll be regenerated.

If the file exists, it'll just use it.

## Tool Configuration
The tool will allow some configuration in order to drive some of the behaviour when
generating graphs.

The configuration file is located in the source directory and is named `msf.yml`.

### Example
In the following example let's imagine that we have 2 API modules and 5 different implementations:

```
api/
  - api-a/
     net.bhservices.test.a.ServiceA
  - api-b/
     net.bhservices.test.b.ServiceB

impl/
  - impl-a1/
     net.bhservices.test.a.impl1.ServiceA1Impl
  - impl-a2/
     net.bhservices.test.a.impl2.ServiceA2Impl
     depends on ServiceB
  - impl-b1/
     net.bhservices.test.b.impl1.ServiceB1Impl
  - impl-b2/
     net.bhservices.test.b.impl2.ServiceB2Impl
     depends on ServiceA
  - impl-b3/
     net.bhservices.test.b.impl3.ServiceB3Impl
```

Now let's say that we want to create an independent microservice for `impl-b2`.
If we let run the tool with no configuration, it'll then depend on by both `impl-a1`
and `impl-a2` which both implements `api-a`.

In this example we would only need one of the implementation to make it work, so then
we would provide the following configuration to force the implementation `impl-a1` to
be used.

Here is a the configuration that would be provided:
```!yaml
implementations:
  - net.bhservices.test.b.impl2.ServiceB2Impl:
      forced-dependencies:
        - net.bhservices.module_a.ServiceA: net.bhservices.test.a.impl1.ServiceA1Impl
```

Now, imagine that we want to create a bundle for `impl-a2` and that we want to select
only `impl-b1` and `impl-b2` for that bundle.

Here is a the configuration that would be provided:
```!yaml
implementations:
  - net.bhservices.test.b.impl2.ServiceB2Impl:
      forced-dependencies:
        - net.bhservices.module_a.ServiceA: net.bhservices.test.a.impl1.ServiceA1Impl

  - net.bhservices.test.a.impl2.ServiceA2Impl:
      forced-dependencies:
        - net.bhservices.test.b.ServiceB:
          - net.bhservices.test.b.impl1.ServiceB1Impl
          - net.bhservices.test.b.impl2.ServiceB2Impl
```

*Note: in this case impl-b2 will be created in a bundle with only impl-a1, but another
bundle will be created for impl-a2 which will also contain impl-b2, but in this case it'll
rely on impl-a2 and not impl-a1 as it already provides the `ServiceA` implementation.*

## Generating dependency graphs
When the tool has finished to generate the indexes, it'll create dependency graphs for each
independent graph found by the tool.

