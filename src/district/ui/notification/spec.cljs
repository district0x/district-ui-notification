(ns district.ui.notification.spec
  (:require [cljs.spec.alpha :as s]))

(s/def ::default-show-duration integer?)
(s/def ::show-duration #(and % integer? pos?))
(s/def ::message string?)
(s/def ::open? boolean?)
(s/def ::notification (s/or :string ::message
                            :map (s/keys :req-un [::message]
                                    :opt-un [::open? ::show-duration])))

(s/def ::queue (s/+ ::notification))
(s/def ::opts (s/nilable (s/keys :req-un [::default-show-duration]
                                 :opt-un [::notification ::queue])))
