(ns repl
  (:require [brainfuck.core :as brainfuck]))

(defn- print-with-flush [msg]
  (do
    (print msg)
    (flush)))

(defn -main [& _]
  (loop []
    (do
      (brainfuck/reset)
      (print-with-flush "brainf*ck> ")
      (let [prompt (read-line)]
        (if (= prompt "exit")
          (print-with-flush "")
          (do
            (brainfuck/exec (vec prompt))
            (print-with-flush "\n")
            (recur)))))))
