(ns clj-go)

(def players [:black :white])

(def player? (set players))

(defn other-player [color]
  (case color
        :white :black
        :black :white
        color))

(defn coords [size]
  (range 1 (inc size)))

(defn vertices [size]
  (for [x (coords size)
        y (coords size)]    
    [x y]))