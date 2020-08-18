(ns repl
  (:require [brainfuck.core :refer [reset exec]]))

(defn- print-with-flush [msg]
  (do
    (print msg)
    (flush)))

(defn -main [& _]
  (loop []
    (do
      (reset)
      (print-with-flush "brainf*ck> ")
      (let [prompt (read-line)]
        (if (= prompt "exit")
          (print-with-flush "")
          (do
            (exec (vec prompt))
            (print-with-flush "\n")
            (recur)))))))
