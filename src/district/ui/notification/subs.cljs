(ns district.ui.notification.subs
  (:require [district.ui.notification.queries :as queries]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::notification
  (fn [db]
    (queries/notification db)))

;; (defn- sub-fn [query-fn]
;;   (fn [db [_ & args]]
;;     (apply query-fn db args)))

;; (reg-sub
;;   ::txs
;;   (sub-fn queries/txs))

;; (reg-sub
;;   ::tx-hashes
;;   (sub-fn queries/tx-hashes))
