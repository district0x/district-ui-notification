(ns tests.all
  (:require
    [cljs.test :refer [deftest is testing run-tests async use-fixtures]]
    [day8.re-frame.test :refer [run-test-async run-test-sync wait-for]]
    [district.ui.district-ui-notification.events :as events]
    [district.ui.district-ui-notification.subs :as subs]
    [district.ui.district-ui-notification]
    [mount.core :as mount]
    [re-frame.core :as re-frame :refer [dispatch subscribe]]))

(use-fixtures :each
  {:before (fn []
             (-> (mount/with-args {:district-ui-notification {:default-show-duration 1000}})
                 (mount/start)))
   :after (fn [] (mount/stop))})

(deftest tests
  (run-test-async
    (let [notification @(subscribe [::subs/district-ui-notification])]

      ;; event accepts arbitrary props
      (dispatch [::events/show {:message "foo"
                                :action-href "bar"}])

      (wait-for [::events/pop-notification]
        (is (= "foo" (:message notification)))
        (is (= "bar" (:action-href notification)))
        (is (= 1000 (:show-duration notification))))

      ;; :show-duration overrides default duration
      (dispatch [::events/show {:message "foo"
                                :show-duration 7000}])

      (wait-for [::events/pop-notification]
        (is (= "foo" (:message notification)))
        (is (= 7000 (:show-duration notification))))

      ;; sync sugar for {:message "abc}
      (dispatch [::events/show "abc"])

      (wait-for [::events/pop-notification]
        (is (= "abc" (:message notification)))
        (is (= 1000 (:show-duration notification))))

      ;; TODO: messages are queued
      )))
