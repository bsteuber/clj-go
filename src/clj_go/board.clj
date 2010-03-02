(ns clj-go.board
  (:use clj-go))

(defn make-board
  "Creates an empty Go board."
  []
  (vec (repeat (* *board-size* *board-size*) :empty)))

(defn board-empty?
  "Is this board empty?"
  [board]
  (every? #{:empty} board))

(defn to-vertex
  "Converts a point (or vertex) to a vertex."
  [p]
  (if (vector? p)
    (+ (* (dec (second p)) *board-size*)
       (dec (first p)))  
    p))

(defn to-point
  "Converts a vertex (or point) to a point." 
  [v]
  (if (vector? v)
            v            
            [(inc (rem v *board-size*))
             (inc (quot v *board-size*))]))

(defn vertex-color
  "Retuns the color of a given board vertex" 
  [board v]
  (board (to-vertex v)))

(defn update-vertex
  "\"Updates\" the color of a given board vertex" 
  [board v color]
  (assoc board (to-vertex v) color))	
  
(defn update-vertices
  "\"Updates\" the color of all given board point indices"
  [board vs color]
  (reduce #(update-vertex %1 %2 color) board vs))

(defn board-stones
  "Returns vectors of vertices of all black and white stones on a given board"
  [board]
  (let [indexed-board (map vector (iterate inc 0) board)]
    (vec (for [color player-colors]
           (vec (for [[i pt] board :when (= color pt)]
                  i))))))