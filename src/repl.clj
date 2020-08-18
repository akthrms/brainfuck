(ns repl
  (:require [brainfuck.core :refer [reset run]]))

(defn- print-with-flush [message]
  (do
    (print message)
    (flush)))

(defn -main []
  (loop []
    (do
      (reset)
      (print-with-flush "brainf*ck> ")
      (let [prompt (read-line)]
        (if (= prompt "exit")
          (print-with-flush "")
          (do
            (run (vec prompt))
            (print-with-flush "\n")
            (recur)))))))
