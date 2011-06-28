(ns go.core)

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

(defn tengen [size]
  (let [x (/ (inc size) 2)]
    [x x]))

(defn handicap-points [size]
  (let [hc-pos  (if (< size 10)
                  3
                  4)
        hc-coords      [hc-pos
                        (/ (inc size) 2)
                        (- (inc size) hc-pos)]]
    (for [x hc-coords
          y hc-coords]
      [x y])))

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
