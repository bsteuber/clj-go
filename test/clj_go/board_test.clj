(ns clj-go.board-test
  (:use clj-go
        clj-go.board
        :reload-all)
  (:use clojure.test))

(def bd19 (make-board))

(deftest empty-board
  (is (board? bd19))
  (is (board-empty? bd19))
  (is (= (* 19 19) (count bd19)))
  (with-board-size 3
    (is (not (board? bd19)))  ; 19x19 boards are no longer boards with other sizes
    (is (= 9 (count (make-board))))))

(deftest vertex-point-conversion
  (is (= (to-vertex 0) 0)) 
  (is (= (to-vertex [1 1]) 0))
  (is (= (to-vertex [2 1]) 1))
  (is (= (to-vertex [19 1]) 18))
  (is (= (to-vertex [1 2]) 19))
  (is (= (to-vertex [19 19]) 360))

  (is (= (to-point [1 1]) [1 1]))
  (is (= (to-point 0) [1 1]))
  (is (= (to-point 1) [2 1]))
  (is (= (to-point 18) [19 1]))
  (is (= (to-point 19) [1 2])))

(deftest board-update
  (letfn [(test-update
           [vertex color]
           (= (-> (make-board)
                  (update-vertex vertex color)
                  (vertex-color vertex))
              color))]
    (with-board-size 9      
      (is (test-update [1 1] :black))
      (is (test-update 3 :black))
      (is (test-update [9 1] :white))
      (is (test-update 80 :white)))))

(deftest board-updates
  (letfn [(test-updates
           [vertices color]
           (let [b (update-vertices (make-board)
                                    vertices
                                    color)]
             (every? #{color}
                     (map #(vertex-color b %) vertices))))]
    (with-board-size 17      
      (is (test-updates [[1 1] [17 17]] :black))
      (is (test-updates [0 (dec (* 17 17))] :black))
      (is (test-updates [[2 1] 53] :white))
      (is (test-updates [[1 2] 85] :white)))))

(deftest stone-lists
  (letfn [(test-stone-lists
           [blacks whites]
           
           )]))