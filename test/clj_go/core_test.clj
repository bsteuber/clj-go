(ns clj-go.core-test
  (:use (clj-go core) :reload)
  (:use [lazytest.describe :only [describe testing it]]))

(describe "coords on 5x5 should"
  (it "be a list from 1 to 5"
    (= (coords 5) [1 2 3 4 5])))

(describe "points on 19x19 should"
  (it "be 361"
    (= 361 (count (points 19))))    
  (it "include corners"
    (every? (points 19)
            [[1 1]
             [1 19]
             [19 1]
             [19 19]]))
  (it "exclude off-board"
    (every? (complement (points 19))
            [[0 5] 
             [7 20]
             [19 -1]
             [20 0]])))

(describe "neighbours"
  (it "in corner"
    (= (neighbours 19 [1 1])
       #{[1 2] [2 1]}))
  (it "on edge"
    (= (neighbours 19 [19 18])
       #{[19 19] [19 17] [18 18]}))  
  (it "in center"
    (= (neighbours 19 [10 10])
       #{[11 10] [9 10] [10 11] [10 9]})))

