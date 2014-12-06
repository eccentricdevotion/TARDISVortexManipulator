#TARDIS Vortex Manipulator

Plugin request: [http://forums.bukkit.org/threads/request-vortex-manipulator.323227/](http://forums.bukkit.org/threads/request-vortex-manipulator.323227/), initiator: roracle

We like a challenge :)

##What it is
"Vortex manipulator. Cheap and nasty time travel. Very bad for you. I'm trying to give it up."

Travel like Jack Harkness or River Song from Doctor Who! Have the Vortex Manipulator in your hand, put in your desired location, and GO!

##Dependencies
This plugin utilises the [TARDIS](http://tardisjenkins.duckdns.org:8080/job/TARDIS/) plugin API, so you must have TARDIS installed as well.

##Crafting
Crafting should be a little complicated, as it's an advanced piece of technology. Requirements for crafting should be (left to right, top row first): stone button, another stone button, glass; clock, gold, compass; iron ingot, iron ingot, iron ingot. Custom graphics for the crafted item and GUI are available as part of the [TARDIS-MCP Resource Pack](https://github.com/eccentricdevotion/TARDIS-MCP).

![Vortex Manipulator crafting recipe](https://dl.dropboxusercontent.com/u/53758864/vortexmanipulator.jpg)
![Vortex Manipulator item](https://raw.githubusercontent.com/eccentricdevotion/Bromley-Massive-Vector/master/assets/minecraft/mcpatcher/cit/vortex_manipulator/manipulator.png)

##Multi-world support
The plugin works with Multiverse (or other multi-word plugin), remembering personally saved locations and allowing for direct teleporting to any available world and coordinate. Also having respect for World Borders (plugin and the 1.8 default if possible). Towny/Faction/WorldGuard/GriefPrevention suport can be enabled via the config.

##Configuration
There are configuration options that allow for energy usage, recharging, and other things. The various features use different levels of energy, while charging is on a timer, gaining so much charge every minute or so.

```
allow:
    teleport: true
    messaging: true
    lifesigns: true
    beacon: true
recipe:
    shape: BBG,WOC,III
    ingredients:
        B: STONE_BUTTON
        G: GLASS
        W: WATCH
        O: GOLD_INGOT
        C: COMPASS
        I: IRON_INGOT
    result: WATCH
    amount: 1
    lore: "Cheap and nasty time travel"
tachyon_use:
    max: 1000
    recharge: 25
    recharge_interval: 600
    travel:
        random: 100
        world: 150
        coords: 200
        saved: 50
    lifesigns: 15
    beacon: 1000
    message: 5
storage:
  database: sqlite
  mysql:
    url: mysql://localhost:3306/TARDIS
    user: bukkit
    password: mysecurepassword
date_format: dd/MM/YY HH:mm
lifesign_scan_distance: 16
respect:
    worldborder: true
    worldguard: true
    griefprevention: true
    factions: true
    towny: true
debug: false
```


##GUI
Because the device in the TV show has buttons and a lot of pushing of them, it would be easiest to have a command based system to work with this - though for hardcore role players, a GUI is provided as well.

![GUI](https://dl.dropboxusercontent.com/u/53758864/vortexmanipulatorGUI.jpg)

##Commands

####Help
* `/vmh {subject}` or `/vm help`  - This will display help and commands for the plugin

####Travel

* `/vm {worldname}` - Teleport to a random location in the specified world
* `/vm {worldname} {X} {Y} {Z}` - Teleport to specific location in the specified world
* `/vm` - Teleport to random world, random location. If another player is standing on the same block as you, they will teleport with you, using more energy. Max of you and two others (or one or just you depending on energy levels of the Vortex Manipulator)
* `/vm go {custom}` - Teleport to saved location

####Saves

* `/vms {custom}` - Save a specific location
* `/vmr {custom}` - Remove saved location

_Other things that should be added, either on initial release or later:_

####Messaging

* `/vmm msg {player} {message}` - Send message to other user with a device
* `/vmm list {in/out} [page]` - See received or sent messages
* `/vmm read {#}` - Read specific message
* `/vmm delete {#}` - Delete specific message
* `/vmm clear {in/out}` - Clearing "in" deletes all inbox messages, and clearing "out" deletes all outbox messages

####Lifesigns

* `/vml` - list entities nearby, mobs and players
* `/vml` {player} - Gets health and hunger of a player, possibly oxygen level too.

####Beacon

* `/vmb` - send out a beacon signal that lasts until the player moves

##Permissions: 
* `vm.teleport` - allow crafting and using of a Vortex Manipulator
* `vm.message` - for messaging system
* `vm.lifesigns` - detect lifesigns and info on players
* `vm.beacon` - activate the beacon feature

##When it'll be released
Time is a funny thing isn't it?

Development builds are available from the [TARDIS Jenkins Server](http://tardisjenkins.duckdns.org:8080/job/TARDISVortexManipulator/lastSuccessfulBuild/me.eccentric_nz.tardisvortexmanipulator$TARDISVortexManipulator/)

