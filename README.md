# nedap.components.assets

A Component bundling [Stefon](https://github.com/nedap/stefon), [Garden](https://github.com/noprompt/lein-garden/) and [WebJars](https://www.webjars.org/) functionality.
Apt for development and production.

The WebJars integration works by extracting specified contents of WebJars .jar files to your project's `resources/assets` directory (this path is configurable).

Then Stefon can pick up those files and process them as it would with any ordinary file.

Note that no default options are provided for Stefon/Garden, as those will vary greatly per project. You can consult the official documentation, or pep-key for sample usage. 

## Installation

```clojure
[com.nedap.staffing-solutions/components.assets "0.1.1"]
````

## ns organisation

There is exactly 1 namespace meant for public consumption:

* `nedap.components.assets.component`

By convention, `api` namespaces are deliberately thin so you can browse them comfortably.

## Documentation

Please browse the public namespaces, which are documented and speced.

## License

Copyright © Nedap

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
