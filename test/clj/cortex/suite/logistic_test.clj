(ns cortex.suite.logistic-test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [cortex.nn.core :as nn]))

;; This test does logistic regression to learn to predict whether people will
;; default based on their balance and income.

;; "Default" dataset: https://cran.r-project.org/web/packages/ISLR/index.html
;; Distributed under the GPL-2 LICENSE
;; The columns are USER_ID,STUDENT?,DEFAULT?,BALANCE,INCOME

(defn default-dataset
  ".csv -> sequence of maps: Could use a real csv parser but doesn't."
  []
  (->> "test/data/default.csv"
      (slurp)
      (s/split-lines)
      (rest) ;; ignore the header row
      (map (fn [l] (drop 2 (s/split l #"," )))) ;; ignore id, student columns
      (mapv (fn [[default balance income]]
             {:data [(Double. balance) (Double. income)]
              :labels (if (= default "\"Yes\"") [1.0] [0.0])}))))

(def network
  [(nn/input 2)
   (nn/batch-normalization)
   (nn/linear->logistic 1)])

(deftest logistic-test
  (nn/reset-training) ;; would delete any cached trained-network files
  (let [dataset (nn/map-sequence->batched-dataset (default-dataset) :observations-per-epoch 500)
        trained-network (nn/train dataset network :batch-size 50 :epoch-count 300)
        [[should-def] [shouldnt-def]] (nn/infer trained-network [[5000.0 10.0] [5.0 100000.0]] :batch-size 2)]
    (is (> should-def 0.97))
    (is (< shouldnt-def 0.02))))
