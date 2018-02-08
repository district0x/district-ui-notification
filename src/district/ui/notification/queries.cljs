(ns district.ui.notification.queries)

(def db-key :district.ui.notification)

(defn assoc-district-ui-notification [db opts]
  (assoc db db-key opts))

(defn dissoc-district-ui-notification [db]
  (dissoc db db-key))

(defn queue-notification [db notification]
  (update-in db [db-key :queue] conj notification))

(defn drop-first-notification [db]
  (update-in db [db-key :queue]
    #(drop 1 %)))

(defn pop-notification [db]
  (-> db
    (get-in [db-key :queue])
    first))

(defn show-notification [db notification]
  (assoc-in db [db-key :notification]
    (cond
      (string? notification)
      {:message notification
       :open? true}

      (map? notification)
      (merge {:open true}
        notification)

      :else notification)))

(defn default-show-duration [db]
  (get-in db [db-key :default-show-duration]))

(defn assoc-open [db value]
  (assoc-in db [db-key :notification :open?] value))

(defn notification [db]
  (get-in db [db-key :notification]))