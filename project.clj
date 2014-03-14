(defproject sor "0.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojurescript "0.0-2173"]
                 [chocolatier "0.1.0-SNAPSHOT"]
                 [crate "0.2.4"]
                 ]
  :plugins [[lein-cljsbuild "1.0.2"]]
  :cljsbuild {
    :builds [
      {
        :id "sor",
        :source-paths ["src"],
        :compiler {
          :pretty-print true,
          :output-dir "static/generated/"
          :output-to "static/generated/main.js",
          :optimizations :none
          :source-map true
          ;:optimizations :whitespace
          ;:source-map "static/generated/main.js.map"
                   }}]})
