# Edge-Gamers CPAS Integration | Spigot API

CpasSpigot is a RESTful API integration plugin which hooks into CpasLibrary by [Agent6262](https://github.com/agent6262/Java-CPAS-Library) and LuckPerms by [Lucko](https://github.com/lucko/LuckPerms). This plugin was made for the [Edge-Gamers Organization](https://edge-gamers.com)

## Dependencies

To handle permissions, LuckPerms is needed. You can view the latest version [here](https://ci.lucko.me/job/LuckPerms/)

Additionally, it is advised to install [Vault](https://travis-ci.org/github/MilkBowl/VaultAPI) for added permission and user management support (though not needed)


## Installation

1) Install needed dependencies seen above.
2) Download latest version of CpasSpigot (or compile if not available)
3) Restart server, let it load, then shut it down to edit files.
4) In configuration, set up the necessary fields, save, and restart server (see below for in-depth tutorial)

## Configuration

You can view the default configuration files as well as an in-depth tutorial on each field [here](https://github.com/garrettpfoy/CpasSpigot/wiki/Configuration)

## Developers

Interested in hooking into CpasSpigot's MAUL component but don't want to mess around with all of the dangers of editing the plugin and breaking something? Me too, so I made an in-depth API that allows you to fetch all of the information Cpas handles and read & write to it which takes affect cross-server. You can view the documentation on API usage [here](https://github.com/garrettpfoy/CpasSpigot/wiki/API-Usage)

## Command & Permissions

You can view what each command does, and how to properly use it [here](https://github.com/garrettpfoy/CpasSpigot/wiki/Commands). 

Similarly, you can view all the permissions CpasSpigot defines, and what each one represents [here](https://github.com/garrettpfoy/CpasSpigot/wiki/Permissions)

## License
[Apache 2.0](https://choosealicense.com/licenses/apache-2.0/)
