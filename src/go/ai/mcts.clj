(ns go.ai.mcts
  (:use (go board)
        (go.ai playout
                   transpositions)))

(def variant-tree (atom nil))

(defn make-node [board color]
  {:wins     1
   :visits   1
   :legal-moves (legal-moves board color)
   :children {}})

(defn add-node [board node]
  (swap! variant-tree assoc
         (hash board) node))

(defn reset-variant-tree [root-board color]
  (reset-hash-table)
  (reset! variant-tree {})
  (add-node root-board
            (make-node root-board color)))

(defn traverse [node board color]

  )