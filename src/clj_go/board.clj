(ns clj-go.board
  (:use clojure.set
        clj-go))

;;; Chains consist of a color and a set of points

(defn make-chain
  "Creates a chain from a given set of stones."
  [color point-set]
  [color point-set])

(defn chain-color
  "The color of a given chain."
  [chain]
  (first chain))

(defn chain-stones
  "The set of points of a given chain."
  [chain]
  (second chain))

;;; A board is a map from points to chains (or nil)

(def #^{:doc "An empty Go board."}
  empty-board
  {})

(defn board-empty?
  "Is this board empty?"
  [board]
  (= board empty-board))

(defn point-color
  "Retuns the color of a given board point." 
  [board point]
  (if-let [chain (board point)]
    (chain-color chain)
    :empty))

(defn add-stone
  "Adds a black or white stone to the given board point."
  [board player point]
  {:pre [(= :empty (point-color board point))]}
  (let [neighbour-chains (map board (neighbours point))
        same-color-chains (filter #(and (not (nil? %))
                                        (= (chain-color %)
                                           player))
                                  neighbour-chains)
        new-chain (make-chain player
                              (apply union
                                     #{point}
                                     (map chain-stones
                                          same-color-chains)))]
    (apply assoc board (interleave (chain-stones new-chain) (repeat new-chain)))))

(defn setup-board
  "Creates a board with all given black and white stones on it."
  [{:keys [blacks whites]}]
  (let [b1 (reduce (fn [board point]
                     (add-stone board :black point))
                   empty-board
                   blacks)
        b2 (reduce (fn [board point]
                     (add-stone board :white point))
                   b1
                   whites)]
    b2))

(defn chain-captured?
  "Tests whether a chain has no liberties left."
  [board chain]
  (not-any? nil?
            (map board
                 (mapcat neighbours (chain-stones chain)))))