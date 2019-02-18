(ns nedap.components.assets.component
  (:require
   [clojure.pprint]
   [com.stuartsierra.component :as component]
   [garden.core]
   [nedap.utils.modular.api :refer [implement]]
   [nedap.utils.io.api :refer [copy-file-from-resource ensure-directory-exists]]
   [stefon.core :as stefon]
   [nedap.utils.spec.api :refer [check!]]
   [nedap.utils.speced :as speced]
   [clojure.spec.alpha :as spec])
  (:import
   (org.webjars WebJarAssetLocator)))

(defn print-all-webjar-assets
  "Prints all available WebJars assets available in the classpath.

  This helps you figure out the config you should pass to this component"
  []
  (-> (WebJarAssetLocator.) (.listAssets "") sort clojure.pprint/pprint))

(defn compile-css! [config]
  (-> config :compiler :output-to ensure-directory-exists)
  (let [{:keys [compiler stylesheet]} config]
    (garden.core/css compiler stylesheet)))

(defn copy-webjars! [{:keys [mappings asset-directory]
                      :or {asset-directory "resources/assets/"}}]
  (doseq [[webjar-name asset-name] mappings]
    (copy-file-from-resource webjar-name
                             (str asset-directory asset-name))))

(defn compile-assets! [{:keys [garden-options stefon-options webjar-options]}]
  (copy-webjars! webjar-options)
  (compile-css! garden-options)
  (stefon/precompile stefon-options))

(defn start [this]
  (compile-assets! this)
  this)

(spec/def ::compiler (spec/keys :req-un [::output-to]))

(speced/def-with-doc ::garden-options
  "A Garden build as per its official doc. Only :compiler and :stylesheet will be used, you can omit the rest."
  (spec/keys :req-un [::compiler ::stylesheet]))

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

(spec/def ::component (spec/keys :req [::garden-options ::stefon-options ::webjar-options]))

(defn new [{::keys [garden-options stefon-options webjar-options]
            :as this}]
  {:pre [(check! ::component this)]}
  (implement this
             component/start start
             component/stop identity))
