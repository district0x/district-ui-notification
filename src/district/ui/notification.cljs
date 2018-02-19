(ns district.ui.notification
  (:require [cljs.pprint :as pprint]
            [cljs.spec.alpha :as s]
            [district.ui.notification.events :as events]
            [district.ui.notification.spec :as spec]
            [mount.core :as mount :refer [defstate]]
            [re-frame.core :as re-frame]))

;; TODO: spec-try ::type value macro
(defn start [opts]
  {:pre [(do (when-not (s/valid? ::spec/opts opts)
               (throw (js/Error (str "Invalid options passed to the component: \n" (s/explain-str ::spec/opts opts)))))
             (s/assert ::spec/opts opts))]}
  (re-frame/dispatch-sync [::events/start opts]))

(defn stop []
  (re-frame/dispatch-sync [::events/stop]))

(defstate district-ui-notification
  :start (start (:district-ui-notification (mount/args)))
  :stop (stop))
