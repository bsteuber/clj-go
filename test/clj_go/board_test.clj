(ns clj-go.board-test
  (:use clj-go
        clj-go.board
        :reload-all)
  (:use clojure.test))

(deftest adding-stones
  (let [b1 (add-stone empty-board :black [1 1])
        b2 (add-stone b1 :black [1 2])
        b3 (add-stone b2 :white [2 2])
        b4 (add-stone b3 :black [5 5])
        c1 [:black #{[1 1]}]
        c2 [:black #{[1 1] [1 2]}]        
        c3 [:white #{[2 2]}]                
        c4 [:black #{[5 5]}]]    
    (is (= b1 {[1 1] c1}))
    (is (= b2 {[1 1] c2, [1 2] c2}))
    (is (= b3 {[1 1] c2, [1 2] c2, [2 2] c3}))
    (is (= b4 {[1 1] c2, [1 2] c2, [2 2] c3, [5 5] c4}))))

(deftest capture
  (let [b (setup-board
            {:blacks [[1 1]]
             :whites [[1 2] [2 1]]})]
    (is (chain-captured? b (b [1 1])))
    (is (not (chain-captured? b (b [1 2])))))
  (let [b (setup-board
            {:blacks [[1 5]]
             :whites [[1 4] [1 6] [2 5]]})]
    (is (chain-captured? b (b [1 5])))
    (is (not (chain-captured? b (b [1 4])))))
  (let [b (setup-board
            {:whites [[4 4]]
             :blacks [[3 4] [5 4] [4 3] [4 5]]})]
    (is (chain-captured? b (b [4 4])))
    (is (not (chain-captured? b (b [3 4]))))))

(deftest moves
  ; TODO
  nil)