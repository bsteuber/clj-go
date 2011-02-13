(ns clj-go.core)

(def other-color {:black :white
                  :white :black})

(defn coords [size]
  (range 1 (inc size)))

(def points
     (memoize (fn [size]
                (apply sorted-set
                       (for [x (coords size)
                             y (coords size)]    
                         [x y])))))

(def neighbours
     (memoize (fn [size [x y]]
                (set
                 (filter (points size)
                         [[(dec x) y]
                          [(inc x) y]
                          [x (dec y)]
                          [x (inc y)]])))))

(defn surrounding [size points]
  (->> points
       (mapcat #(neighbours size %))
       (remove (apply hash-set points))
       distinct))