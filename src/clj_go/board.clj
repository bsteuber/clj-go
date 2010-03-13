(ns clj-go.board
  (:use clojure.set
        clj-go))

;;; Chains are one of:
;;; - a pair of a color and a set of occupied points
;;; - nil

(defn make-chain
  "Creates a chain from a given set of occupied points."
  [player stones]
  [player stones])

(defn chain-player
  "The player of a given chain - or nil when given nil."
  [[player _]]
  player)

(defn chain-stones
  "The set of points identifying the stones of a given chain - or nil when given nil"
  [[_ points]]
  points)

;;; A board is a map from points to chains (or nil)

(def #^{:doc "An empty Go board."}
  empty-board
  {})

(defn board-empty?
  "Is this board empty?"
  [board]
  (= board empty-board))

(defn update-board
  "Updates all given board points to map to the same given chain."
  [board chain points]
  (into board (zipmap points (repeat chain))))

(defn neighbour-chains-of-player
  "A sequence of all chains of the given player that are adjacent to a point."
  [board player point]
  (->> point
       neighbours
       (map board)
       (filter #(= player (chain-color %)))))

(defn put-stone
  "Puts a stone on a given empty board point."
  [board player point]
  {:pre [(not (board point))]}
  (let [new-chain (->> (neighbour-chains-of-player board player point)
                       (map chain-stones)
                       (apply union #{point})
                       (make-chain player))]
    (update-board board new-chain (chain-stones new-chain))))

(defn put-stones
  "Puts stones of the same player on all given empty board points."
  [board player points]
  (reduce (fn [board point]
            (put-stone board player point))
          board
          points))

(defn setup-board
  "Creates a board with all given black and white stones on it."
  [{:keys [blacks whites]}]
  (-> empty-board
      (put-stones :black blacks)
      (put-stones :white whites)))

(defn chain-captured?
  "Tests whether a chain has no liberties left."
  [board chain]
  (->> chain
       chain-stones
       (mapcat neighbours)
       (map board)
       (not-any? nil?)))

(defn move-at
  "Plays a move of the given color on an empty board point.
   Returns :suicide instead of a new board when the move is committing suicide."
  [board player point]
  (let [board (put-stone board player point)
        enemy-chains (neighbour-chains-of-player board
                                                 (other-player player)
                                                 point)
        captured-enemy-chains (filter chain-captured? enemy-chains)]
    (if-not (empty? captured-enemy-chains)
      (update-board board nil (apply concat captured-enemy-chains))
      (if (chain-captured? (board point))
        :suicide
        board))))