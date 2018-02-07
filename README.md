# district-ui-notification

Clojurescript [mount](https://github.com/tolitius/mount) + [re-frame](https://github.com/Day8/re-frame) module for a district UI, that provides core logic for transaction notofications. This module does not provide [reagent](https://github.com/reagent-project/reagent) UI component for a notifications snackabr, only
logic to build the component upon. This way many different reagent components can be build on top of this module.

## Installation
Add `[district0x/district-ui-notification "1.0.0"]` into your project.clj
Include `[district.ui.district-ui-notification]` in your CLJS file, where you use `mount/start`

## API Overview

**Warning:** district0x modules are still in early stages of development, therefore API can change in the future.

TODO


## Dependency on other district UI modules

TODO

## Development
```bash
lein deps
# To run tests and rerun on changes
lein doo chrome tests
```
