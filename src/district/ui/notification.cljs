(ns district.ui.notification
  (:require [cljs.spec.alpha :as s]
            [district.ui.notification.events :as events]
            [mount.core :refer [defstate]]
            [re-frame.core :as re-frame]))

(s/def ::default-show-duration integer?)
(s/def ::show-duration integer?)
(s/def ::message string?)
(s/def ::open? boolean?)
(s/def ::notification (s/keys :req-un [::message]
                              :opt-un [::open? ::show-duration]))
(s/def ::queue (s/+ ::notification))
(s/def ::opts (s/nilable (s/keys :req-un [::default-show-duration]
                                 :opt-un [::notification ::queue])))

(defn start [opts]
  {:pre [(s/assert ::opts opts)]}
  (re-frame/dispatch-sync [::events/start opts]))

(defn stop []
  (re-frame/dispatch-sync [::events/stop]))

(defstate district-ui-notification
  :start (start (:district-ui-notification (mount/args)))
  :stop (stop))
