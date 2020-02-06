# SimpleProtect
 A minecraft protect plugin

### Function

* Protect farm form jumper
* Eliminate block damage of explosion
* Prevent creatures from piling up

### Command

`/protect entity [world STR]` Display Entity Info
`/protect entity purge <TYPE|all> [world STR] [rate INT]` Purge Entity
`/protect chunk Display` Chunk Info
`/protect chunk unload` Free Chunks

### Permission

`simpleprotect.admin` Use `protect` command. Default: OP

### Configuration

```
SpawnMob:
  # This option protect mob farm lag your server
  # {value} same mobs allowed to spawn in a range
  enable: true
  value: 16
  
KeepFarm:
  # This option protect your farm from jumper
  enable: true
  ExclusiveMode: false
  world:
  - world
  
AntiExplosion:
  # This option prevent remove block from a explosion
  enable: true
  ExclusiveMode: false
  world:
  - world
  
PurgeExclusion:
  # This list prevent entity remove
  # Check eneity id here: 
  # https://minecraft-zh.gamepedia.com/index.php?title=%E6%95%B0%E6%8D%AE%E5%80%BC/%E5%AE%9E%E4%BD%93ID
  - 8
  - 9
  - 18
  - 21
  - 30
  - 40
  - 41
  - 42
  - 43
  - 44
  - 45
  - 46
  - 47
```

