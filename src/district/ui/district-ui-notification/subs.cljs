(ns district.ui.district-ui-notification.subs
  (:require [district.ui.district-ui-notifications.queries :as queries]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::district-ui-notification
  (fn [db]
    (queries/district-ui-notification db)))

;; (defn- sub-fn [query-fn]
;;   (fn [db [_ & args]]
;;     (apply query-fn db args)))

;; (reg-sub
;;   ::txs
;;   (sub-fn queries/txs))

;; (reg-sub
;;   ::tx-hashes
;;   (sub-fn queries/tx-hashes))
