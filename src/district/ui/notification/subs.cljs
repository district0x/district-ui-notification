(ns district.ui.notification.subs
  (:require [district.ui.notification.queries :as queries]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::notification
 (fn [db]
   ;; TODO
   (prn "@DB"  db)
   (queries/notification db)))

(re-frame/reg-sub
 ::peek-notification
 (fn [db]
   (queries/peek-notification db)))
