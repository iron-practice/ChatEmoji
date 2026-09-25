[![modrinth](https://img.shields.io/modrinth/v/ChatEmoji?label=Modrinth&style=for-the-badge)](https://modrinth.com/plugin/chatemoji)

# ChatEmoji

Player head emojis in chat, using the object text components added in Minecraft 1.21.9.

ChatEmoji replaces ``:name:`` key in chat messages with player head emojis.
Players can list all emojis with ``/emoji``.

There's a server style that can be switched trough with ``/emoji style <default/apple>``. only with permission ``chatemoji.admin``. (style also changeable in ``config.yml``)

Enable only players with permission ``chatemoji.use`` in the ``config.yml``.

## Configuration

By default everyone can use emojis.

## Installation

Drop the jar into your `plugins` folder and restart the server.

## Building

Clone the repository, `cd` into it, then `./gradlew build`.

The output file will be located in `./build/libs/ChatEmoji-VERSION.jar`.
