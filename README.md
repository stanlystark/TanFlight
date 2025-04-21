# TanFlight

__Required__: [[TaN] Towns & Nations](https://www.spigotmc.org/resources/tan-towns-nations.114019/)

Just fly inside your towns and regions.

___

## Commands:

- /tanfly - Enable/disable flight.
- /tanfly reload - Reload the config.

---

## Permission nodes:

- tanflight.command.fly - required to use `/tanfly`.
- tanflight.command.reload - required to use `/tanfly reload`.
- tanflight.fly.region - fly only in region.
- tanflight.fly.town - fly only in town.
- tanflight.fly - fly to your regions and towns.

---

## Config:

- options.autoFlight = __false__
    - If active, flight will automatically turn on when entering a town or region where you can fly.
- options.autoFlightMessage = __false__
    - If active, players will receive a notification in the chat that the flight is activated.
