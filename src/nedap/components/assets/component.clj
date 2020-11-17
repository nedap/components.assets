(ns nedap.components.assets.component
  (:require
   [clojure.pprint]
   [clojure.spec.alpha :as spec]
   [com.stuartsierra.component :as component]
   [garden.core]
   [nedap.speced.def :as speced]
   [nedap.utils.io.api :refer [copy-file-from-resource ensure-directory-exists]]
   [nedap.utils.modular.api :refer [implement]]
   [stefon.core :as stefon])
  (:import
   (org.webjars WebJarAssetLocator)))

(spec/def ::compiler (spec/keys :req-un [::output-to]))

(speced/def-with-doc ::run-in-background?
  "Whould the component run in background as it `start`s, returing immediately?

This option is only apt for development."
  boolean?)

(speced/def-with-doc ::garden-options
  "A Garden build as per its official doc. Only :compiler and :stylesheet will be used, you can omit the rest."
  (spec/keys :req-un [::compiler ::stylesheet]))

(speced/def-with-doc ::garden-options-collection
  "A collection of garden options."
  (spec/coll-of ::garden-options :min-count 1))

(speced/def-with-doc ::stefon-options
  "A Stefon map, as per its official doc."
  map?)

(speced/def-with-doc ::webjars.asset-directory
  "The relative path to where this component will copy WebJars assets,
extracting them from .jars, for later processing by Stefon."
  string?)

(speced/def-with-doc ::webjars.mappings
  "Maps WebJars resource names (e.g.: META-INF/resources/webjars/tether/1.4.0/js/tether.js) that you want to consume,
to the filenames that will be created in your project (e.g. javascript/webjars/tether.js).

:webjars.asset-directory is used as a prefix.

You can use `print-all-webjar-assets` in order to figure out the resource names."
  (spec/map-of string? string?))

(spec/def ::webjar-options (spec/keys :req [::webjars.mappings]
                                      :opt [::webjars.asset-directory]))

(spec/def ::component (spec/keys :req [::garden-options-collection ::stefon-options ::webjar-options]
                                 :opt [::run-in-background?]))

(defn print-all-webjar-assets
  "Prints all available WebJars assets available in the classpath.

  This helps you figure out the config you should pass to this component"
  []
  (-> (WebJarAssetLocator.) (.listAssets "") sort clojure.pprint/pprint))

(speced/defn compile-css! [^::garden-options config]
  (-> config :compiler :output-to ensure-directory-exists)
  (let [{:keys [compiler stylesheet]} config]
    (garden.core/css compiler stylesheet)))

(speced/defn copy-webjars! [{^::webjars.mappings mappings               ::webjars.mappings
                             ^::webjars.asset-directory asset-directory ::webjars.asset-directory}]
  (let [asset-directory (or asset-directory "resources/assets/")]
    (doseq [[webjar-name asset-name] mappings]
      (copy-file-from-resource webjar-name
                               (str asset-directory asset-name)))))

(speced/defn compile-assets! [{::keys [garden-options-collection
                                       ^::stefon-options stefon-options
                                       webjar-options]}]
  (copy-webjars! webjar-options)
  (doseq [garden-options garden-options-collection]
    (compile-css! garden-options))
  (stefon/precompile stefon-options))

(defn start [{::keys [run-in-background?]
              :as    this}]
  (letfn [(impl []
            (with-out-str ;; silence Garden
              (compile-assets! this)))]
    (if run-in-background?
      (future
        (impl))
      (impl))
    this))

(speced/defn new [^::component this]
  (implement this
    component/start start
    component/stop  identity))
