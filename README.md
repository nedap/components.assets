# nedap.components.assets

A Component bundling [Stefon](https://github.com/nedap/stefon), [Garden](https://github.com/noprompt/lein-garden/) and [WebJars](https://www.webjars.org/) functionality.
Apt for development and production.

The WebJars integration works by extracting specified contents of WebJars .jar files to your project's `resources/assets` directory (this path is configurable).

Then Stefon can pick up those files and process them as it would with any ordinary file.

Note that no default options are provided for Stefon/Garden, as those will vary greatly per project. You can consult the official documentation, or pep-key for sample usage. 

## Installation

```clojure
[com.nedap.staffing-solutions/components.assets "2.0.0"]
````

#### NOTE

You may have to apply ` exclusions [com.google.guava/guava com.google.protobuf/protobuf-java com.google.javascript/closure-compiler]` in order to prevent a ClojureScript compilation from popping up (which doesn't make sense in a JVM project) and failing the build.

## ns organisation

There is exactly 1 namespace meant for public consumption:

* `nedap.components.assets.component`

## Documentation

Please browse the public namespaces, which are documented and speced.

## License

Copyright © Nedap

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0.
