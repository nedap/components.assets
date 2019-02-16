(defproject com.nedap.staffing-solutions/components.assets "0.1.1"
  :description "Clojure Component bundling Stefon, Garden and WebJars functionality."
  :url "https://github.com/nedap/components.assets"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"releases"       {:url      "https://nedap.jfrog.io/nedap/staffing-solutions/"
                                   :username :env/artifactory_user
                                   :password :env/artifactory_pass}}
  :deploy-repositories [["releases" {:url "https://nedap.jfrog.io/nedap/staffing-solutions/"
                                     :sign-releases false}]]
  :repository-auth {#"https://nedap.jfrog\.io/nedap/staffing-solutions/"
                    {:username :env/artifactory_user
                     :password :env/artifactory_pass}}
  :dependencies [[com.nedap.staffing-solutions/stefon "0.5.0"]
                 [com.nedap.staffing-solutions/utils.io "0.1.0"]
                 [com.nedap.staffing-solutions/utils.modular "0.1.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [garden "1.3.5"]
                 [org.clojure/clojure "1.10.0"]
                 [org.webjars/webjars-locator "0.27"]
                 ;; Stefon needs it. We ensure a recent version is used, as the default (older) has an issue.
                 [ring/ring-core "1.5.0"]])
