(ns clj-go.ai.playout
  (:use (clj-go core
                board)))

(def default-komi 6.5)

(defn ponnuki? [board color point]
  (every? (of-col?-fn board color)
          (neighbours (:size board)
                      point)))

(defn pseudo-eye? [board color point]
  (and (ponnuki? board color point)
       (not (play board (other-color color) point))))

(defn random-playout-strategy [board color]
  (loop [free-points (remove board
                             (board-points board))]
    (when-not (empty? free-points)
      (let [next-move (rand-nth free-points)
            new-board (play board color next-move)]
        (if (and new-board
                 (not (pseudo-eye? board color next-move)))
              new-board
              (recur (remove #{next-move}
                             free-points)))))))

(defn score-point [board point]
  (case (board point)
        :black -1
        :white 1
        ;;else
        (let [b-score (if (play board :black point) 1 0)
              w-score (if (play board :white point) 1 0)]
          (- w-score b-score))))

(defn winner [final-board komi]
  (let [score (->> (board-points final-board)
                   (map #(score-point final-board %))
                   (reduce + komi))]
    (println score)
    (cond
     (pos? score) :white
     (neg? score) :black
     :else        :jigo)))

(defn playout [board color strategy]
  (let [next-board (strategy board color)
        next-color (other-color color)]
    (if next-board
      (recur next-board next-color strategy)
      (if-let [next-board (strategy board next-color)]
        (recur next-board color strategy)
        (do
          (println "Final position"(format-board board))
          (winner board default-komi))))))