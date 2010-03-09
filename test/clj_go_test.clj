(ns clj-go-test
  (:use clj-go
        :reload-all)
  (:use [clojure.test]))

(deftest coords
  (is (coord? 1))
  (is (coord? 19))
  (is (not (coord? 0)))
  (is (not (coord? -1)))
  (is (not (coord? 20)))
  (is (= (mirror-coord 1) 19))
  (is (= (mirror-coord 10) 10))
  (with-board-size 4
    (is (coord? 4))
    (is (not (coord? 5)))
    (is (mirror-coord 1) 4)
    (is (mirror-coord 2) 3)))

(deftest points
  (is (point? [1 1]))
  (is (point? [1 19]))
  (is (point? [19 1]))
  (is (point? [19 19]))
  (is (not (point? [0 5]))) 
  (is (not (point? [7 20])))
  (is (not (point? [19 -1])))
  (is (not (point? [20 0])))
  (with-board-size 6
    (is (point? [6 6]))
    (is (not (point? [6 7])))))

(deftest neighbourhood
  (is (= (neighbours [1 1])
         #{[1 2] [2 1]}))
  (is (= (neighbours [19 18])
         #{[19 19] [19 17] [18 18]}))  
  (is (= (neighbours [10 10])
         #{[11 10] [9 10] [10 11] [10 9]}))
  (with-board-size 10
    (is (= (neighbours [10 10])
         #{[10 9] [9 10]}))))
