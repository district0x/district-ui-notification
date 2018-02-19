(ns district.ui.notification.events
  (:require [day8.re-frame.async-flow-fx]
            [day8.re-frame.forward-events-fx]
            [district0x.re-frame.interval-fx]
            [district0x.re-frame.spec-interceptors :as d0x-spec-interceptors]
            [district.ui.notification.spec :as spec]
            [district.ui.notification.queries :as queries]
            [re-frame.core :as re-frame]
            [cljs.pprint :refer [pprint]]))

(def interceptors [re-frame/trim-v])

(re-frame/reg-event-fx
 ::start
 [interceptors]
 (fn [{:keys [db]} [opts]]
   {:db (queries/assoc-district-ui-notification db opts)
    :forward-events {:register queries/db-key
                     :events #{::queue-notification}
                     :dispatch-to [::process-queue]}}))

(re-frame/reg-event-fx
 ::stop
 [interceptors]
 (fn [{:keys [:db]}]
   {:db (queries/dissoc-district-ui-notification db)}))

(re-frame/reg-event-db
 ::init
 [interceptors]
 (fn [db [opts]]
   (queries/assoc-district-ui-notification db opts)))

(re-frame/reg-event-fx
 ::show
 [interceptors (d0x-spec-interceptors/validate-first-arg ::spec/notification)]
 (fn [{:keys [db]} [notification]]
   {:dispatch [::queue-notification notification]}))

(re-frame/reg-event-db
 ::queue-notification
 [interceptors]
 (fn [db [notification]]
   (queries/queue-notification db notification)))

(re-frame/reg-event-db
 ::clear-queue
 [interceptors]
 (fn [db]
   (queries/clear-queue db)))

(re-frame/reg-event-fx
 ::process-queue
 [interceptors]
 (fn [{:keys [:db]} ]
   (let [queue (queries/queue db)
         events (loop [queue queue
                       this (peek queue)
                       events [{:delay 0 :notification this}]]
                  (if (empty? queue)
                    events
                    (recur (pop queue)
                           (peek (pop queue))
                           (let [{:keys [delay]} (last events)
                                 {:keys [show-duration]} this
                                 next (peek (pop queue))]
                             (conj events {:delay (+ delay
                                                     (if show-duration
                                                       show-duration
                                                       (queries/default-show-duration db)))
                                           :notification next})))))]
     {:dispatch-n (mapv #(vec [::show-next-notification %])
                        events)
      :dispatch-later [{:ms (-> events last :delay)
                        :dispatch [::clear-queue]}]})))

(re-frame/reg-event-fx
 ::show-next-notification
 [interceptors]
 (fn [{:keys [:db]} [{:keys [delay notification]}]]
   {:dispatch-later [{:ms delay
                      :dispatch [::show-notification notification]}]}))

(re-frame/reg-event-fx
 ::hide-notification
 [interceptors]
 (fn [{:keys [db]} _]
   {:db (-> db
            (queries/assoc-open false))}))

(re-frame/reg-event-db
 ::show-notification
 [interceptors]
 (fn [db [notification]]
   (queries/show-notification db notification)))
