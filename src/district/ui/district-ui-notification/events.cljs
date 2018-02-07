(ns district.ui.district-ui-notification.events
  (:require [day8.re-frame.async-flow-fx]
            [day8.re-frame.forward-events-fx]
            [district0x.re-frame.spec-interceptors :as d0x-spec-interceptors]
            [district.ui.district-ui-notification.queries :as queries]
            [re-frame.core :as re-frame]
            ;;[vimsical.re-frame.cofx.inject :as inject]
            ))

(def interceptors [re-frame/trim-v])

;; TODO : intercept and spec

(re-frame/reg-event-fx
  ::start
  [interceptors]
  (fn [{:keys [db]} [opts]]
    {:db (queries/assoc-district-ui-notification db opts)
     :forward-events {:register :listen-notifications
                      :events #{::queue-notification}
                      :dispatch-to [::pop-notification]}}))

(re-frame/reg-event-fx
  ::stop
  [interceptors]
  (fn [{:keys [:db]}]
    {:db (queries/dissoc-district-ui-notification db)}))

(re-frame/reg-event-db
  ::queue-notification
  [interceptors]
  (fn [db [notification]]
    (queries/queue-notification db notification)))

(re-frame/reg-event-fx
  ::pop-notification
  [interceptors]
  (fn [{:keys [db]} _]
    {:db (queries/remove-first-notification db)
     :dispatch [::show-notification (queries/pop-notification db)]}))

(re-frame/reg-event-fx
  ::show-notification
  [interceptors]
  (fn [{:keys [db]} [{:keys [show-duration] :as notification}]]
    {:db (queries/show-notification db notification)
     :dispatch-later [{:ms (if show-duration
                             show-duration
                             (queries/default-show-duration))
                       :dispatch [::hide-notification]}]}))

(re-frame/reg-event-fx
  ::hide-notification
  [interceptors]
  (fn [{:keys [db]} _]
    {:db (queries/assoc-open db false)}))

(re-frame/reg-event-fx
  ::show
  [interceptors]
  (fn [{:keys [db]} [notification]]
    {:dispatch [::queue-notification notification]}))
