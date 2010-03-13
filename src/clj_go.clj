(ns clj-go)

(def #^{:doc "The two player colors black and white"}
     players
     [:black :white])

(defn other-player
  "Gives the other player's color."
  [color]
  (case color
        :white :black
        :black :white))

(def
 #^{:doc "The board size - usually 19, but can be rebound using with-board-size."}
 *board-size*
 19)

(defmacro with-board-size
  "Evaluates the body with a dynamically rebound board size."
  [size & body]
  `(binding [*board-size* ~size]       
     ~@body))

(defn coord?
  "Tests if something is a legal (one-based) board coordinate, i.e. between 1 and *board-size*"
  [coord]
  (and (number? coord)
       (<= 1 coord *board-size*)))

(defn mirror-coord
  "Mirrors a given coord."
  [coord]
  {:pre [(coord? coord)]}
  (- (inc *board-size*) coord))

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
  [point]
  (and (vector? point)
       (= (count point) 2)       
       (every? coord? point)))

(def #^{:doc "The four directions left, right, up and down"}
 directions
 [[-1 0] [1 0] [0 -1] [0 1]])

(defn neighbours
  "The set of all (on-board) neighbours of a given point"
  [point]
  (into #{}
        (filter point?
                (map #(vec (map + point %))
                     directions))))