(ns clj-go.board
  (:use clj-go)
  (:require [clojure.string :as str]))

(defrecord ^{:doc "A squared go board of a give size"}
  Board
  [sz]
  clojure.lang.IFn
  (invoke [b pt]
          (get b pt)))

(defn put
  "A board with stones added at points. For removing stones, use dissoc."
  [board color & points]
  (merge board
         (zipmap points
                 (repeat color))))

(defn- neighb-fn [board]
  #(neighbours (:sz board) %))

(defn- of-col?-fn [board color]
  #(= (board %) color))

(defn chain
  "The set of all points connected to the chain at point"
  [board point]
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

(defn liberties
  "The set of liberties of the chain at point"
  [board point]
  (->> (chain board point)
       (mapcat (neighb-fn board))
       (filter (of-col?-fn board nil))
       set))

(defn captured?
  "Is the chain at point captured?"
  [board point]
  (empty? (liberties board point)))

(defn play
  "The position after a move at point.
  Nil is returned when
    - point is not on board
    - point is occupied
    - move is illegal.
  Doesn't check for ko rule."
  [board color point]
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

(def char->col {\x :black
                \o :white})

(def col->char {:black \x
                :white \o
                nil    \.})

(defn read-board
  "Read a board from a string like \n...\n.xo\n.o."
  [s]
  (let [lines (-> s
                  str/trim
                  (str/replace " " "")
                  str/split-lines)        
        size (count lines)]
    (into (Board. size)
          (filter second
                  (for [[x y :as p] (points size)]
                    [p (char->col (nth (nth lines (dec y))
                                       (dec x)))])))))

(defn format-board
  "Formats a board to a string like \n...\n.xo\n.o."
  [{sz :sz :as board}]
  (str "\n"
       (str/join "\n" (for [y (coords sz)]
                             (str/join (for [x (coords sz)]
                                         (col->char (board [x y]))))))
       "\n"))

(defmethod print-method Board [bd writer]
           (binding [*out* writer] (print (format-board bd))))
