# Meedl-Leude Minecraft Plugin

**Aktuelle Version:** 1.0 Release
**Beschreibung:** Ein vielseitiges Admin-Plugin für Minecraft, das Statusverwaltung, Inventarverwaltung, Spielerverwaltung und Serverinformationen ermöglicht.

---

## Hauptfunktion

### `/status`
Zeigt den aktuellen Status des Spielers an.

---

## Befehle und Berechtigungen

| Befehl         | Nutzbar ohne Berechtigung | Berechtigung |
|----------------|--------------------------|--------------|
| `/status`      | ✅                        | -            |
| `/bastard`     | ✅                        | -            |
| `/setstatus`   | ❌                        | `meedlleude.setstatus` |
| `/removestatus`| ❌                        | `meedlleude.setstatus` |
| `/serverhealth`| ❌                        | `meedlleude.health` |
| `/inventory`   | ❌                        | `meedlleude.inventory` |
| `/enderchest`  | ❌                        | `meedlleude.enderchest` |
| `/regeln`      | ✅                        | -            |
| `/mute`        | ❌                        | `meedlleude.mute` |
| `/unmute`      | ❌                        | `meedlleude.unmute` |
| `/ban`         | ❌                        | `meedlleude.ban` |
| `/unban`       | ❌                        | `meedlleude.unban` |


> Hinweis: Für den eigenen Status wird keine Berechtigung benötigt.

---

## Sandbox Platzhalter Übersicht

| Platzhalter    | Chat | Tablist | MotD | Bedeutung |
|----------------|------|---------|------|-----------|
| `{status}`     | ✅    | ✅      | ❌    | Status (Prefix) vom Spieler, leer wenn nicht gesetzt |
| `{playername}` | ✅    | ✅      | ❌    | Spielername |
| `{msg}`        | ✅    | ❌      | ❌    | Nachricht des Spielers |
| `{world}`      | ✅    | ✅      | ❌    | Name der Welt |
| `{worldtype}`  | ✅    | ✅      | ❌    | Overworld, Nether, End |
| `{hh}`         | ✅    | ✅      | ✅    | Stunden |
| `{mm}`         | ✅    | ✅      | ✅    | Minuten |
| `{ss}`         | ✅    | ✅      | ✅    | Sekunden |
| `{version}`    | ❌    | ❌      | ✅    | Version als Ganzes (lange Zahl) |
| `{mcversion}`  | ❌    | ❌      | ✅    | Minecraft Version des Servers |
| `{bukkitversion}` | ❌ | ❌      | ✅    | Bukkit Version |
| `{maxmb}`      | ❌    | ❌      | ✅    | Maximal verfügbarer RAM (in MB) |
| `{freemb}`     | ❌    | ❌      | ✅    | Freier RAM (in MB) |
| `{usedmb}`     | ❌    | ❌      | ✅    | Genutzter RAM (in MB) |
| `{slots}`      | ❌    | ❌      | ✅    | Maximale Anzahl an Spielern |
| `{onlineplayers}` | ❌ | ❌      | ✅    | Anzahl der aktuell online Spieler |
| `{servername}` | ❌    | ❌      | ✅    | Name des Servers aus den Servereinstellungen |
