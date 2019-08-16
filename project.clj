;; Please don't bump the library version by hand - use ci.release-workflow instead.
(defproject com.nedap.staffing-solutions/components.assets "1.0.0"
  ;; Please keep the dependencies sorted a-z.
  :dependencies [[com.nedap.staffing-solutions/stefon "0.5.0"
                  :exclusions [clj-time commons-codec com.fasterxml.jackson.core/jackson-core]]
                 [com.nedap.staffing-solutions/utils.io "1.0.0"
                  :exclusions [org.apache.commons/commons-compress]]
                 [com.nedap.staffing-solutions/utils.modular "2.0.0"]
                 [com.stuartsierra/component "0.4.0"]
                 [garden "1.3.5"]
                 [org.apache.commons/commons-compress "1.18"]
                 [org.clojure/clojure "1.10.1"]
                 [org.webjars/webjars-locator "0.27"
                  :exclusions [org.apache.commons/commons-compress]]
                 ;; Stefon needs it. We ensure a recent version is used, as the default (older) has an issue:
                 [ring/ring-core "1.5.0"]]

  :description "Clojure Component bundling Stefon, Garden and WebJars functionality."

  :url "https://github.com/nedap/components.assets"

  :min-lein-version "2.0.0"

  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :signing {:gpg-key "releases-staffingsolutions@nedap.com"}

  :repositories {"releases" {:url      "https://nedap.jfrog.io/nedap/staffing-solutions/"
                             :username :env/artifactory_user
                             :password :env/artifactory_pass}}

  :repository-auth {#"https://nedap.jfrog\.io/nedap/staffing-solutions/"
                    {:username :env/artifactory_user
                     :password :env/artifactory_pass}}

  :target-path "target/%s"

  :monkeypatch-clojure-test false

  :plugins [[lein-pprint "1.1.2"]]

  ;; A variety of common dependencies are bundled with `nedap/lein-template`.
  ;; They are divided into two categories:
  ;; * Dependencies that are possible or likely to be needed in all kind of production projects
  ;;   * The point is that when you realise you needed them, they are already in your classpath, avoiding interrupting your flow
  ;;   * After realising this, please move the dependency up to the top level.
  ;; * Genuinely dev-only dependencies allowing 'basic science'
  ;;   * e.g. criterium, deep-diff, clj-java-decompiler

  ;; NOTE: deps marked with #_"transitive" are there to satisfy the `:pedantic?` option.
  :profiles {:dev {:dependencies [[cider/cider-nrepl "0.16.0" #_"formatting-stack needs it"]
                                  [com.clojure-goes-fast/clj-java-decompiler "0.2.1"]
                                  [com.stuartsierra/component "0.4.0"]
                                  [com.taoensso/timbre "4.10.0"]
                                  [criterium "0.4.4"]
                                  [formatting-stack "1.0.0-alpha3"
                                   :exclusions [rewrite-clj]]
                                  [lambdaisland/deep-diff "0.0-29"]
                                  [medley "1.1.0"]
                                  [org.clojure/core.async "0.4.490"]
                                  [org.clojure/math.combinatorics "0.1.1"]
                                  [org.clojure/test.check "0.10.0-alpha3"]
                                  [org.clojure/tools.namespace "0.3.0-alpha4"]
                                  [org.clojure/tools.reader "1.1.1" #_"transitive"]
                                  [rewrite-clj "0.6.1" #_"transitive"]]
                   :plugins      [[lein-cloverage "1.0.13"]]
                   :source-paths ["dev" "test"]
                   :repl-options {:init-ns dev}}

             :ci  {:pedantic?    :abort
                   :jvm-opts     ["-Dclojure.main.report=stderr"]
                   :global-vars  {*assert* true} ;; `ci.release-workflow` relies on runtime assertions
                   :dependencies [[com.nedap.staffing-solutions/ci.release-workflow "1.3.0-alpha3"]]}})
