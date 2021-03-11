# DiscordBot
A multipurpose discord bot that heavily dabbles in moderation tools and utilities.

This bot is still in an alpha base, and currently has no versioning semantics behind it. The estimated date for 
stable production release is mid March pending on some factors. 

Below you will find all the libraries and tools that DiscordBot utilizes to help it maintain and run as <i>efficiently</i> as possible.

While DiscordBot isn't available to the public yet, the source is. I am only a novice developer still in my own eyes, but I wanted to start documenting
my progress towards becoming a better developer over time, which can hopefully be shown through the progression of this bot.

--------------------------
### Libraries and Tools
* [JDA](https://github.com/DV8FromTheWorld/JDA)
  
    DiscordBot is developed in Java, utilizing the JDA library which wraps the Discord API. Without it, DiscordBot would not exist. So we are thankful for the maintainers and contributors of the JDA library.


* [ExpiringMap](https://github.com/jhalterman/expiringmap)

    This tool allows us to implement a thread-safe <b>ConcurrentMap</b> system to help with caching entities, allowing for expiration policies and the low-overhead involved with it. There are no plans in the future to switch to Caffeine or another library of the likes until ExpiringMap becomes deprecated or has breaking changes.


* [HikariCP](https://github.com/brettwooldridge/HikariCP)

    This tool provides a "zero-overhead" JDBC connection pool to allow for better database handling.


* [jOOQ](https://github.com/jOOQ/jOOQ)

    A library that helps minimize the use of the JPA standard, allowing for better SQL interaction: allowing for type safe queries, and reducing overall redundancy.

----
