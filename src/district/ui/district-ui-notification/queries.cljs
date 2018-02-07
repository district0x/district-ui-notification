(ns district.ui.district-ui-notification.queries
  (:require [district.ui.web3-tx.queries :as tx-queries]))

(defn assoc-district-ui-notification [db opts]
  (assoc db :district.ui.district-ui-notification opts))

(defn dissoc-district-ui-notification [db]
  (dissoc db :district.ui.district-ui-notification))

(defn queue-notification [db notification]
  (update-in db [:district.ui.district-ui-notification :notifications] conj notification))

(defn remove-first-notification [db]
  (update-in db [:district.ui.district-ui-notification :queue]
    #(drop 1 %)))

(defn pop-notification [db]
  (-> db
    (get-in [:district.ui.district-ui-notification :queue])
    first))

(defn show-notification [db notification]
  (assoc-in db [:district.ui.district-ui-notification :notification]
    (cond
      (string? notification)
      {:message notification
       :open? true}

      (map? notification)
      (merge {:open true}
        notification)

      :else notification)))

(defn default-show-duration [db]
  (get-in db [:district.ui.district-ui-notification :default-show-duration]))

(defn assoc-open [db value]
  (assoc-in db [:district.ui.district-ui-notification :notification :open?] value))

(defn district-ui-notification [db]
  (:district.ui.district-ui-notification db))
