# ChatEmoji

Player head emojis in chat, using the object text components added in Minecraft 1.21.9.

Type `:smile:` in chat and it renders as a player head. `/emoji` lists all available emojis.

Requires Paper 1.21.9 or newer. No dependencies.

## Configuration

Emojis are defined in `config.yml` as `name: base64 texture` pairs (the "Value" field on
[minecraft-heads.com](https://minecraft-heads.com)). `/emoji` lists them in config order.

By default everyone can use emojis. To restrict them, set `permission.required: true` and
grant the `chatemoji.use` node (configurable via `permission.node`). The deny message is
configurable in MiniMessage format under `messages.no-permission`.

## Installation

Drop the jar into your `plugins` folder and restart the server.

## Building

Clone the repository, `cd` into it, then `./gradlew build`.

The output file will be located in `./build/libs/ChatEmoji-VERSION.jar`.
