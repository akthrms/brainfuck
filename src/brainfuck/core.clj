(ns brainfuck.core)

; メモリ
(def memory (atom (vec (repeat 30000 0))))

; ポインタ
(def pointer (atom 0))

; 文字位置
(def index (atom 0))

; ポインタが指す値を取得する
(defn- get-reference []
  (nth @memory @pointer))

; メモリ・ポインタ・文字位置を初期化する
(defn reset []
  (do
    (reset! memory (vec (repeat 30000 0)))
    (reset! pointer 0)
    (reset! index 0)))

(defmulti command (fn [file-chars idx] (identity (nth file-chars idx))))

; ポインタを加算する
(defmethod command \> [& _]
  (swap! pointer inc))

; ポインタを減算する
(defmethod command \< [& _]
  (swap! pointer dec))

; ポインタが指す値を加算する
(defmethod command \+ [& _]
  (swap! memory assoc @pointer (inc (get-reference))))

; ポインタが指す値を減算する
(defmethod command \- [& _]
  (swap! memory assoc @pointer (dec (get-reference))))

; ポインタが指す値を出力する
(defmethod command \. [& _]
  (print (char (get-reference))))

; ポインタが指す値に入力する
(defmethod command \, [& _]
  (swap! memory assoc @pointer (read)))

; ループを開始する
(defmethod command \[ [file-chars _]
  (when (zero? (get-reference))
    (loop [bracket-count 1]
      (when (> bracket-count 0)
        (do
          (swap! index inc)
          (case (nth file-chars @index)
            \[ (recur (inc bracket-count))
            \] (recur (dec bracket-count))
            (recur bracket-count)))))))

; ループを終了する
(defmethod command \] [file-chars _]
  (when (not (zero? (get-reference)))
    (loop [bracket-count 1]
      (when (> bracket-count 0)
        (do
          (swap! index dec)
          (case (nth file-chars @index)
            \] (recur (inc bracket-count))
            \[ (recur (dec bracket-count))
            (recur bracket-count)))))))

; それ以外はスキップする
(defmethod command :default [& _])

; 実行
(defn exec [file-chars]
  (loop [idx @index]
    (when (> (count file-chars) idx)
      (command file-chars idx)
      (swap! index inc)
      (recur @index))))
