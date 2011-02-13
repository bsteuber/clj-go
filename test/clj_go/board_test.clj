(ns clj-go.board-test
  (:use (clj-go core board)
        :reload)
  (:use [lazytest.describe :only [describe testing it]]))

(def s1 "...
         .x.
         .oo")

(def s2 "...
         .xx
         .oo")

(def s3 "...
         .xx
         x..")

(def b1 (make-board 3
          :black [[2 2]]
          :white [[2 3] [3 3]]))

(describe "reading"
  (it "from string"
    (= b1 (read-board s1)))
  (it "from printed board"
    (= b1 (read-board (format-board b1)))))

(describe "liberties"
  (it "should work"
    (= (liberties b1 [2 3])
       (liberties b1 [3 3])
       #{[1 3] [3 2]})))

(describe "capture"
  (it "single stones"
    (captured? (read-board ".o.
                            oxo
                            .oo")
               [2 2]))
  (it "chains"
    (captured? (read-board "...
                            .xx
                            xoo")
               [3 3])))

(describe "play moves"
    (it "without capture"
      (= (play b1 :black [3 2])
         (read-board "...
                             .xx
                             .oo")))
    (it "with capture 1 stone"
      (-> (read-board "...
                       xo.
                       o.o")
          (play :black [2 3])
          (= (assoc (read-board  "...
                                  xo.
                                  .xo")
               :ko [1 3]))))
    (it "with capture 2 stones"
      (= (play (read-board s2) :black [1 3])
         (read-board "...
                             .xx
                             x..")))
    (it "ko"
      (-> (read-board "...
                       xo.
                       o.o")
          (play :black [2 3])
          (play :white [1 3])
          nil?)))
