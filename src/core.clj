(ns core
  (:require [brainfuck.core :as brainfuck]))

(defn -main [filename & _]
  (do
    (brainfuck/reset)
    (with-open [reader (clojure.java.io/reader filename)]
      (->> (line-seq reader)
           (clojure.string/join "\n")
           (vec)
           (brainfuck/exec)))))
