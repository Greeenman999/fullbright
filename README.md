# Fullbright

This is a fabric mod that adds a fullbright feature to the game.

## Installation
Download the jar from [modrinth](https://modrinth.com/mod/fullbright) and install the following <u>dependencies</u>.

[![](https://github.com/Prospector/badges/blob/master/modrinth-badge-72h-padded.png?raw=true)](https://modrinth.com/mod/fullbright)

Or clone the github repository and execute `./gradlew buildAndCollect`.
After completion the compiled jar should appear in  `build/libs/`.

## Dependencies
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
- [Mod Menu (Optional)](https://modrinth.com/mod/modmenu)

## Usage

1. Make sure all the dependencies are installed.
2. Use `/fullbright toggle` or the keybind (default: `B`) to toggle fullbright on and off.
3. Use `/fullbright strength <value>` to set the brightness strength (default: `10`, max: `10`, min: `0`).
4. Use `/fullbright config` or [Mod Menu](https://modrinth.com/mod/modmenu) to access the configuration screen.
5. Have fun!

### If you have any further issues, create an issue on [my github](https://github.com/Greeenman999/fullbright/issues) or message me on discord `@greenman999`.

## License

This project is licensed under the GNU General Public License v3.

### Third‑party libraries, code and licenses

This project includes some code from [Vigilance](https://github.com/EssentialGG/Vigilance) and the ui library [Elementa](https://github.com/EssentialGG/Elementa),
licensed under the GNU Lesser General Public License v3. See [NOTICE.md](NOTICE.md) for details.