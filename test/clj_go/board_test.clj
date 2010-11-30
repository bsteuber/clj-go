(ns clj-go.board-test
  (:use clj-go
        clj-go.board
        :reload)
  (:import (clj-go.board Board))
  (:use clojure.test))

(def s1 "...
         .x.
         .oo")

(def b1 (-> (Board. 3)
              (put :black [2 2])
              (put :white [2 3] [3 3])))

(deftest readb0
  (is (= (read-board s1)
         b1))
  (is (= (read-board (format-board b1))
         b1)))


(deftest neighbs
  (is (= (liberties b1 [2 3])
         (liberties b1 [3 3])
         #{[1 3] [3 2]})))

(deftest capture
  (is (captured? (put b1 :black [3 2] [1 3])
                 [3 3]))
  (is (captured? (put b1 :white [1 2] [2 1] [3 2])
                 [2 2])))

(deftest playing
  (let [b2 (play b1 :black [3 2])
        b3 (play b2 :black [1 3])]
    (is (= b2 (read-board "...
                           .xx
                           .oo")))

    (is (= b3 (read-board "...
                           .xx
                           x..")))))
