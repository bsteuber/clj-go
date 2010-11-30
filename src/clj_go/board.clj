(ns clj-go.board
  (:use clj-go)
  (:require [clojure.string :as str]))

(defrecord Board [sz]
  clojure.lang.IFn
  (invoke [b pt]
    (get b pt)))

(defn put [board color & points]
  (merge board
         (zipmap points
                 (repeat color))))

(def strip-spaces #(str/replace % " " ""))

(defn like [& args]
  (apply = (map (comp str/trim strip-spaces)
                args)))

(def char->col {\x :black
                \o :white})

(def col->char {:black \x
                :white \o
                nil    \.})

(defn read-board [s]
  (let [lines (str/split-lines
               (strip-spaces s))
        size (count lines)]
    (into (Board. size)
          (filter second
                  (for [[x y :as p] (points size)]
                    [p (char->col (nth (nth lines (dec y))
                                       (dec x)))])))))

(defn format-board [{sz :sz :as board}]
  (str "\n"
       (str/join "\n" (for [y (coords sz)]
                             (str/join (for [x (coords sz)]
                                         (col->char (board [x y]))))))
       "\n"))

(defmethod print-method Board [bd writer]
           (binding [*out* writer] (print (format-board bd))))

(defn neighb-fn [board]
  #(neighbours (:sz board) %))

(defn of-col?-fn [board color]
  #(= (board %) color))

(defn chain [board point]
  (let [color (board point)]
    (loop [current [point]
           chain #{}
           visited #{}]
      (if (empty? current)
        chain        
        (recur (remove visited
                       (mapcat (neighb-fn board) current))
               (into chain
                     (filter (of-col?-fn board color)
                             current))
               (into visited current))))))

(defn liberties [board point]
  (->> (chain board point)
       (mapcat (neighb-fn board))
       (filter (of-col?-fn board nil))
       set))

(defn captured? [board point]
  (empty? (liberties board point)))

(defn play [board color point]
  (let [opp (other-color color)
        sz (:sz board)
        new-board (put board color point)
        opp-neighbs (filter (of-col?-fn board opp)
                            (neighbours sz point))
        captured-neighbs (filter #(captured? new-board %) opp-neighbs)
        suicide? (and (empty? captured-neighbs)
                      (captured? new-board point))]
    (if (and ((points sz) point)
             (not (board point))
             (not suicide?))
      (apply dissoc new-board (mapcat #(chain board %)
                                      captured-neighbs)))))