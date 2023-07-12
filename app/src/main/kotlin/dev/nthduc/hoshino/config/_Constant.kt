/**
 * This file contains configuration values for the bot, such as the bot token, default prefix, and Lavalink server information.
 * The values are read from environment variables using the dotenv library.
 *
 * @author: nthduc
 */
package dev.nthduc.hoshino.config

import io.github.cdimascio.dotenv.dotenv // Import the dotenv library to read environment variables from a .env file

val dotenv = dotenv() // Create a new instance of the dotenv object
val BOT_TOKEN = dotenv["BOT_TOKEN"] // Read the BOT_TOKEN environment variable from the .env file
val DEFAULT_PREFIX = "ah!" // Set the default prefix for bot commands
val APPLICATION_ID = dotenv["APPLICATION_ID"] // Read the APPLICATION_ID environment variable from the .env file
val LAVALINK_SERVER = dotenv["LAVALINK_SERVER"] // Read the LAVALINK_SERVER environment variable from the .env file
val LAVALINK_PASSWORD = dotenv["LAVALINK_PASSWORD"] // Read the LAVALINK_PASSWORD environment variable from the .env file
