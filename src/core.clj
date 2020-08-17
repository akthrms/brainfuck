(ns core)

; メモリ
(def memory (atom (vec (repeat 512 0))))

; ポインタ
(def pointer (atom 0))

; 文字位置
(def index (atom 0))

; ポインタが指す値を取得する
(defn- get-reference []
  (nth @memory @pointer))

; メモリ・ポインタ・文字位置を初期化する
(defn- initial []
  (do
    (reset! memory (vec (repeat 512 0)))
    (reset! pointer 0)
    (reset! index 0)))

(defmulti execute (fn [c _] (identity c)))

; ポインタを加算する
(defmethod execute \> [& _]
  (swap! pointer inc))

; ポインタを減算する
(defmethod execute \< [& _]
  (swap! pointer dec))

; ポインタが指す値を加算する
(defmethod execute \+ [& _]
  (swap! memory assoc @pointer (inc (get-reference))))

; ポインタが指す値を減算する
(defmethod execute \- [& _]
  (swap! memory assoc @pointer (dec (get-reference))))

; ポインタが指す値を出力する
(defmethod execute \. [& _]
  (print (char (get-reference))))

; ポインタが指す値に入力する
(defmethod execute \, [& _]
  (swap! memory assoc @pointer (read)))

; ループを開始する
(defmethod execute \[ [_ file-chars]
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
(defmethod execute \] [_ file-chars]
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
(defmethod execute :default [& _])

; メイン
(defn -main [filename & _]
  (do
    (initial)
    (with-open [reader (clojure.java.io/reader filename)]
      (let [file-chars (->> (line-seq reader)
                            (clojure.string/join "\n")
                            (vec))]
        (loop [i @index]
          (when (> (count file-chars) i)
            (execute (nth file-chars i) file-chars)
            (swap! index inc)
            (recur @index)))))))
