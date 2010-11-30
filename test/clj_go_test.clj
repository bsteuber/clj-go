(ns clj-go-test
  (:use clj-go
        :reload-all)
  (:use [clojure.test]))

(deftest test-coords
  (is (= (coords 5)) [1 2 3 4 5]))

(deftest test-points
  (let [p (points 19)]
    (is (= (count p) 361))
    (is (p [1 1]))
    (is (p [1 19]))
    (is (p [19 1]))
    (is (p [19 19]))
    (is (not (p [0 5]))) 
    (is (not (p [7 20])))
    (is (not (p [19 -1])))
    (is (not (p [20 0])))))

(deftest test-neighbours
  (is (= (neighbours 19 [1 1])
         #{[1 2] [2 1]}))
  (is (= (neighbours 19 [19 18])
         #{[19 19] [19 17] [18 18]}))  
  (is (= (neighbours 19 [10 10])
         #{[11 10] [9 10] [10 11] [10 9]})))

