# district-ui-notification

TODO: CI badge

Clojurescript [mount](https://github.com/tolitius/mount) + [re-frame](https://github.com/Day8/re-frame) module for a district UI, that provides core logic for transaction notofications. This module does not provide [reagent](https://github.com/reagent-project/reagent) UI component for notifications, only
logic to build the component upon. This way many different reagent components can be build on top of this module.

## Installation
Add `[district0x/district-ui-notification "1.0.0"]` into your project.clj
Include `[district.ui.notification]` in your CLJS file, where you use `mount/start`

## Usage

**Warning:** district0x modules are still in early stages of development, therefore API can change in the future.

- [district.ui.notification](#districtuinotification)
- [district.ui.notification.events](#districtuinotificationevents)
  - [::show](#show)
  - [::show-notification](#shownotification)
  - [::hide-notification](#hidenotification)
- [district.ui.notification.subs](#districtuinotificationsubs)
  - [::notification](#notification)
- [district.ui.notification.queries](#districtuinotificationqueries)
  - [queue-notification](#queuenotificationquery)
  - [pop-notification](#popnotificationquery)
  - [peek-notification](#peeknotificationquery)
  - [show-notification](#shownotificationquery)
  - [hide-notification](#hidenotificationquery)
  - [notification](#notificationquery)
- [district.ui.notification.spec](#districtuinotificationspec)
  - [::notification](#notification-spec)

## <a name="districtuinotification"> district.ui.notification

This namespace contains district-ui-notification [mount](https://github.com/tolitius/mount) module.

You can pass following args to initiate this module:
* `:default-show-duration` Specifies the default amount of time (in milliseconds) the notification will be displayed for.

```clojure
(ns district.ui.core
  (:require [mount.core :as mount]
            [district.ui.notification]))

(-> (mount/with-args {:district-ui-notification {:default-show-duration 1000}})
      (mount/start))
```

## <a name="districtuinotificationevents"> district.ui.notification.events

re-frame events provided by this module:

#### <a name="show"> `::show`

This is typically the only event you should ever need. Queues the next notification to be displayed.
You can pass the following arguments:

* `:message` to be displayed
* `show-duration` (which overrides the default `default-show-duration`)
* arbitrary number of other arguments

```clojure
(ns district.ui.core
  (:require [re-frame.core :as re-frame]
            [district.ui.notification.events :as events]))

(re-frame/dispatch [::events/show
                    {:show-duration 3000
                     :message "FOO"
                     :foo "bar"}])
```

If no `:show-duration` argument is provided in the arguments map, the `:default-show-duration` is assumed:

```clojure
(re-frame/dispatch [::events/show {:message "FOO"}])
```

You can also dispatch this event with a string as the only argument:

```clojure
(re-frame/dispatch [::events/show "FOO"])
```

which is just a synctatic sugar for the former.

#### <a name="shownotification"> `::show-notification`

Sets current notification to be displayed, bypassing the queue.

#### <a name="hidenotification"> `::hide-notification`

Sets `:open` of the current notification to `false`.

## <a name="districtuinotificationsubs"> district.ui.notification.subs

re-frame subscriptions provided by this module:

#### <a name="notification"> `::notification`

This is typically the only subscription you will need. Returns current notification that is first in queue.
Subscription returns a map with key-value pairs:

* `:open?` false if notification has already been displayed for `:show-duration` (or `:default-show-duration`) amount of time.
* `:message`
*  Other arguments as passed to the [`::show`](#show) event

```clojure
(ns my-district
  (:require [district.ui.notification.subs :as subs]
            [re-frame.core :as re-frame]))

(let [{:keys [:open? :message :foo]} @(re-frame/subscribe [::subs/notification])]
  (prn message))
```

## <a name="districtuinotificationqueries"> district.ui.notification.queries

DB queries provided by this module:
*You should use them in your events or subscriptions.*

#### <a name="queuenotificationquery"> `queue-notification`

Adds a notification to the end of queue.

#### <a name="popnotificationquery"> `pop-notification`

Removes the first notification in queue.

#### <a name="peeknotificationquery"> `peek-notification`

Return the first notifictaion in queue, does not alter the queue.

#### <a name="shownotificationquery"> `show-notification`

Sets the current notification to be displayed.

#### <a name="hidenotificationquery"> `hide-notification`

Sets `:open?` key of the current notification to false.

#### <a name="notificationquery"> `notification`

Returns the current notification.

## <a name="districtuinotificationspec"> district.ui.notification.spec

specs provided by this module:

#### <a name="notification-spec">`::notification`

This is typically the only spec you will need. Defines the valid argument for the [`::show`](#show) event.

```clojure
(ns my-district
  (:require [cljs.spec.alpha :as s]
            [district.ui.notification.spec :as spec]))

(s/valid? ::spec/notification {:show-duration 3000
                               :message "FOO"
                               :foo "bar"})
```

## Development

Run test suite:

```bash
lein deps
# To run tests and rerun on changes
lein doo chrome tests
```
Install into local repo:

```bash
lein cljsbuild test
lein install
```
