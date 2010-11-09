(ns clj-go.board
  (:use clj-go)
  (:require [clojure.string :as str]))

(defn board [size]
  (merge (zipmap (vertices size)
                 (repeat :empty))
         {:size size}))

(defn put [board color & points]
  (merge board
         (zipmap points
                 (repeat color))))

(def strip-spaces #(str/replace % " " ""))

(defn read-board [s]
  (let [lines (str/split-lines
               (strip-spaces s))
        size (count lines)]
    (into {:size size}
          (for [[x y :as p] (vertices size)]
            [p (case (nth (nth lines
                               (dec y))
                          (dec x))
                     \x :black
                     \o :white
                     \. :empty)]))))

(defn print-board [{sz :size :as board}]
  (str/join "\n" (for [y (coords sz)]
                   (str/join (for [x (coords sz)]
                               (case (board [x y])
                                     :black "x"
                                     :white "o"
                                     :empty "."))))))

(defn neighbours [board [x y]]
  (filter board
          [[(inc x) y]
           [(dec x) y]
           [x (inc y)]
           [x (dec y)]]))

(defn chain [board point]
  (let [color (board point)]
    (if (player? color)
      (loop [current [point]
             chain #{}
             visited #{}]
        (recur (remove visited
                       mapcat neighbours current)
               (into chain
                     (filter #(= (board %)
                                 color)
                             current))
               (into visited current))))))