(ns go.board
  (:use go.core)
  (:require [clojure.string :as str]))

(defn put
  "A board with stones added at points. For removing stones, use dissoc."
  [board color & points]
  (merge board
         (zipmap points
                 (repeat color))))

(defn make-board
  "Creates a go board of a given size.
  Can be given lists of initial black and white stones."
  [size & {:keys [black white]}]
  (let [put-fn #(apply put %1 %2 %3)]
    (-> {:size size}
        (put-fn :black black)
        (put-fn :white white))))

(def board-points (comp points :size))

(defn board-stones [board]
  (filter vector? (keys board)))

(defn neighb-fn [board]
  #(neighbours (:size board) %))

(defn of-col?-fn [board color]
  #(= (board %) color))

(defn chain
  "The set of all points connected to the chain at point"
  [board point]
  (let [color (board point)
        neighbs (neighb-fn board)
        right-color? (of-col?-fn board color)]
    (loop [current #{point}
           chain #{}
           visited #{}]
      (if (empty? current)
        chain
        (let [current-with-right-color (filter right-color?
                                               current)]
          (recur (->> current-with-right-color
                      (mapcat neighbs)
                      (remove visited)
                      set)
                 (->> current-with-right-color
                      (into chain))
                 (into visited current)))))))

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

(defn one-element? [seq]
  (if (counted? seq)
    (= (count seq) 1)
    (and (first seq)
         (not (next seq)))))

(defn atari?
  [board point]
  (one-element? (liberties board point)))

(defn play
  "Gives the board after a move at point.
  Nil is returned when
    - point is not on board
    - point is occupied
    - move is illegal.
  Checks for simple ko rule."
  [board color point]
  (let [opp              (other-color color)
        size             (:size board)
        new-board        (put board color point)
        opp-neighbs      (filter (of-col?-fn board opp)
                                 (neighbours size point))
        captured-neighbs (filter #(captured? new-board %) opp-neighbs)
        suicide?         (and (empty? captured-neighbs)
                              (captured? new-board point))
        ko               (when (one-element? captured-neighbs)
                           (let [captured-chain
                                 (chain board
                                        (first captured-neighbs))]
                             (when (one-element? captured-chain)
                               (first captured-chain))))
        new-board        (if ko
                           (assoc new-board :ko ko)
                           (dissoc new-board :ko))]
    (when (and ((points size) point)
               (not (board point))
               (not (and ko
                         (= (:ko board)
                            point)))
               (not suicide?))
      (apply dissoc new-board (mapcat #(chain board %)
                                      captured-neighbs)))))

(defn legal-moves [board color]
  (->> (board-points board)
       (remove board)
       (filter #(play board color %))))

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
    (into (make-board size)
          (filter second
                  (for [[x y :as p] (points size)]
                    [p (char->col (nth (nth lines (dec y))
                                       (dec x)))])))))

(defn format-board
  "Formats a board to a string like \n...\n.xo\n.o."
  [{size :size :as board}]
  (str "\n"
       (str/join "\n" (for [y (coords size)]
                             (str/join (for [x (coords size)]
                                         (col->char (board [x y]))))))
       "\n"))
