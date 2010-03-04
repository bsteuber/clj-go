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

(def #^{:doc "The square of the board size - usually 361, but can be rebound using with-board-size."}
     *squared-board-size*
     361)

(defmacro with-board-size
  "Evaluates the body with a dynamically rebound board size."
  [size & body]
  `(let [size# ~size]
     (binding [*board-size* size#
               *squared-board-size* (* size# size#)]       
       ~@body)))

(defn coord?
  "Tests if something is a legal (one-based) board coordinate, i.e. between 1 and *board-size*"
  [c]
  (and (number? c)
       (<= 1 c *board-size*)))

(defn mirror-coord
  "Mirrors a given coord."
  [x]
  {:pre [(coord? x)]}
  (- (inc *board-size*) x))

(defn handicap-coords
  "The (one-based) coords of all handicap stone positions."
  []
  (case *board-size*
        9  [5]
        13 [4 10]
        19 [4 10 16]
           []))

(defn point?
  "Tests if something is a legal board point, i.e. a vector of two coords"
  [p]
  (and (vector? p)
       (= (count p) 2)       
       (every? coord? p)))