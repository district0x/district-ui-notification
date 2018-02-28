(ns tests.all
  (:require
   [cljs.test :refer [deftest is testing run-tests async use-fixtures]]
   [day8.re-frame.test :refer [run-test-async run-test-sync wait-for]]
   [district.ui.notification.events :as events]
   [district.ui.notification.subs :as subs]
   [district.ui.notification]
   [mount.core :as mount]
   [re-frame.core :as re-frame]))

(use-fixtures :each
  {:before (fn [] (-> (mount/with-args {:district-ui-notification {:default-show-duration 1000}})
                      (mount/start)))
   :after (fn [] (do (mount/stop)))})

(deftest tests
  (run-test-async
   (let [notification (re-frame/subscribe [::subs/notification])]

     ;; TEST: event accepts arbitrary props
     (re-frame/dispatch [::events/show {:message "foo"
                                        :action-href "bar"}])

     (wait-for [::events/show-notification]
               (is (= "foo" (:message @notification)))
               (is (= "bar" (:action-href @notification)))
               (wait-for [::events/clear-queue]))

     ;; TEST: sync sugar

     (re-frame/dispatch [::events/show "abc"])

     (wait-for [::events/show-notification]
               (is (= "abc" (:message @notification)))
               (wait-for [::events/clear-queue]))

     ;; TEST: override default-show-duration

     (re-frame/dispatch [::events/show {:message "foo"
                                        :show-duration 2000}])

     (wait-for [::events/show-notification]
               (let [tick (.getTime (js/Date.))]
                 (wait-for [::events/clear-queue]
                           (let [duration (- (.getTime (js/Date.)) tick)]
                             (is (= "foo" (:message @notification)))
                             ;; notification should be displayed for 2 seconds (+/- 10 percent)
                             (is (and (>= duration 1800) (<= duration 2200))))))))))
