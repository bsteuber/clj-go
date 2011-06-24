(ns go.ladder
  (:use (go core board)))

(declare ladder-captured?)

(defn ladder-capturable? [board point]
  (or (nil? board)
      (let [libs (liberties board point)
            defender (board point)
            attacker (other-color defender)]
        (case (count libs)
              1 true
              2 (some #(ladder-captured?
                        (play board attacker %)
                        point)
                      libs)
              false))))

(defn ladder-captured? [board point]
  (and (not (nil? board))       
       (let [libs (liberties board point)
             defender (board point)
             attacker (other-color defender)]
         (and (= (count libs) 1)
              (let [escape-move (first libs)
                    opp-chains  (->> (chain board point)
                                     (surrounding (:size board))
                                     (filter (of-col?-fn board attacker))
                                     (map chain)
                                     distinct)
                    capturing-moves (->> opp-chains
                                         (map #(liberties board (first %)))
                                         (filter #(= (count %)
                                                     1))
                                         (map first))
                    moves-to-try (cons escape-move capturing-moves)]
                (every? #(ladder-capturable?
                          (play board defender %)
                          point)
                        moves-to-try))))))
