[license]: https://github.com/MaukiMC/MaukiSeasonPL/blob/main/LICENSE
[license-shield]: https://img.shields.io/badge/License-GPL3.0-green.svg

[version]: https://img.shields.io/static/v1?label=Download&message=SNAPSHOT-v1.18.0_ALPHA&color=blue
[download]: https://github.com/MaukiMC/MaukiSeasonPL/releases/tag/SNAPSHOT-v1.18.0_ALPHA

[wiki]: https://img.shields.io/static/v1?label=Javadoc&message=Overview&color=orange
[wikilink]: https://maukimc.github.io/MaukiSeasonPL/

[ ![version][] ][download]
[ ![license-shield][] ][license]
[ ![wiki][] ][wikilink]
# MaukiSeasonPL

## Table of contents
1. [Introduction](#introduction)
2. [Building the .jar](#building-the-jar)
3. [Dependencies](#dependencies)

## Introduction
This Plugin is for Magma 1.16.5 servers specially programmed for the 
Mauki-Minecraft Seasons made with Gradle as module-manager. 
The Plugin is supposed to manage the gameplay on the server and provide an API 
for the [dashboard](https://web.mc.mauki.net) to interact with the server as a 
player. 

## Building the .jar
You can build the plugin in the gradle demon with the following command:
````shell
$ build :copypa01 :copypa02
````
You will find the .jar file at ``out/jar/MaukiSeasonPL.jar``.

## Dependencies
This plugin uses the following dependencies (group:name:version)
- com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT
- de.taimos:totp:1.0
- commons-codec:commons-codec:1.15
- com.google.zxing:javase:3.5.1
- net.dv8tion:JDA:5.0.0-beta.2
- org.xerial:sqlite-jdbc:3.40.1.0
- com.github.Mokulu:discord-oauth2-api:1.0.2
- io.github.cdimascio:dotenv-java:3.0.0
- org.java-websocket:Java-WebSocket:1.5.3

This plugin also uses [JavaDiscordWebhookClient](https://github.com/MiauRizius/JavaDiscordWebhookClient) made by [MiauRizius](https://github.com/MiauRizius)
