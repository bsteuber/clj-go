(defproject clj-go "0.1-SNAPSHOT"
  :description "A general library for the asian game of Go"
  :dependencies [[org.clojure/clojure "1.3.0-alpha3"]
                 #_[org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[swank-clojure "1.3.0-SNAPSHOT"]
                     [com.stuartsierra/lazytest "1.2.3"]
                     [lein-lazytest "1.0.1"]]
  :lazytest-path ["src" "test"]
  :repositories {"stuartsierra-releases" "http://stuartsierra.com/maven2"})