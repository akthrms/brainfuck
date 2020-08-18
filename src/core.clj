(ns core
  (:require [brainfuck.core :refer [reset run]]))

(defn -main [filename & _]
  (do
    (reset)
    (with-open [reader (clojure.java.io/reader filename)]
      (->> (line-seq reader)
           (clojure.string/join "\n")
           (vec)
           (run)))))
