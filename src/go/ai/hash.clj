(ns go.ai.hash)

(def hash-table
  (atom nil))

(defn reset-hash-table []
  (reset! hash-table
          {::count 0}))

(defn hash [board]
  (or (@hash-table board)
      (let [count (::count @hash-table)]
        (swap! hash-table assoc
               ::count (inc count)
               board   count))))