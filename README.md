# BixisHub

Core lobby mechanics plugin for YeditepeMC network.
Handles essential lobby behavior: adventure mode, hunger management,
spawn system, and join/quit message suppression.

## Features
- Automatic adventure mode on join
- Hunger bar always full (never decreases)
- Custom spawn point system
- Join/quit messages suppressed
- Respawn teleports to lobby spawn
- Void protection: players falling below a configured Y level are teleported to spawn

## Commands
| Komut | Açıklama | Permission |
|-------|----------|------------|
| `/setspawn` | Mevcut konumu spawn olarak kaydet | `bixishub.admin` |
| `/spawn` | Spawn noktasına ışınlan | Herkes |

## Requirements
- Paper 1.21.11+ (single jar, also runs on Paper 26.1.2)
- Java 21+

## Installation
1. Drop BixisHub.jar into plugins/
2. Restart server
3. Go to desired spawn location and run `/setspawn`