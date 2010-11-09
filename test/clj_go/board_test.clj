(ns clj-go.board-test
  (:use clj-go
        clj-go.board
        :reload)
  (:use clojure.test))

(def s1 "...
         .x.
         .oo")

(def b1 (-> (board 3)
              (put :black [2 2])
              (put :white [2 3] [3 3])))

(deftest readb
  (is (= (read-board s1)
         b1))
  (is (= (strip-spaces s1)
         (print-board b1))))


