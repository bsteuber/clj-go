(ns clj-go)

(def player-colors [:black :white])
(def board-colors (conj player-colors :empty))

(defn other-player
  "Gives the other player's color."
  [color]
  (case color
        :white :black
        :black :white))

(def #^{:doc "The board size - usually 19, but can be rebound using with-board-size."}
     *board-size*
     19)

(defmacro with-board-size
  "Evaluates the body with a dynamically rebound board size."
  [size & body]
  `(binding [*board-size* ~size]
     ~@body))

(defn mirror-coord [x]
  "Mirrors a given coord."
  (- (inc *board-size*) x))

(defn handicap-coords
  "The (one-based) coords of all handicap stone positions."
  []
  (case *board-size*
        9  [5]
        13 [4 10]
        19 [4 10 16]
           []))

(defn point-on-board?
  "Tests if a point is on the board."
  [point]
  (every? #(<= 1 % *board-size*) point))

