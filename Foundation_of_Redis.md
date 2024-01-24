# Requirements:
 - Problem statement
 - Why it matters
 - Theory and technology base
 - How to solve them
 - Demo
 - Presentation slides

# References
https://aws.amazon.com/redis/

https://backendless.com/redis-what-it-is-what-it-does-and-why-you-should-care/#1670599762368-db4223a0-cc7a

https://www.linkedin.com/pulse/why-when-use-redis-hamed-moayeri

http://oldblog.antirez.com/post/take-advantage-of-redis-adding-it-to-your-stack.html

https://redis.io/docs/about/

# Problem statement

## Slow latest items listings in your home page
Can I have a penny for every instance of the following query that is running too slow please?
SELECT * FROM foo WHERE ... ORDER BY time DESC LIMIT 10
To have listings like "latest items added by our users" or "latest something else" in web applications is very common, and often a scalability problem. It is pretty counter intuitive that you need to sort stuff if you just want to list items in the same order they were created.

## Leaderboards and related problems
Another very common need that is hard to model with good performances in DBs that are not in-memory is to take a list of items, sorted by a score, updated in real time, with many updates arriving every second.

The classical example is the leaderboard in an online game, for instance a Facebook game, but this pattern can be applied to a number of different scenarios. In the online game example you receive a very high number of score updates by different users. WIth this scores you usually want to:
Show a leaderboard with the top #100 scores.
Show the user its current global rank.

## Order by user votes and time
A notable variation of the above leaderboard pattern is the implementation of a site like Reddit or Hacker News, where news are ordered accordingly to a forumla similar to:
score = points / time^alpha
So user votes will raise the news in a proportional way, but time will take the news down exponentially. Well the actual algorithm is up to you, this will not change our pattern.

This pattern works in this way, starting from the observation that probably only the latest, for instance, 1000 news are good candidates to stay in the home page, so we can ignore all the others. The implementation is simple:
Every time a new news is posted we add the ID into a list, with LPUSH + LTRIM in order to take only the latest 1000 items.
There is a worker that gets this list and continually computes the final score of every news in this set of 1000 news. The result is used to populate a sorted set with ZADD. Old news are removed from the sorted set in the mean time as a cleanup operation.

## Unique N items in a given amount of time
Another interesting example of statistic that is trivial to do using Redis but is very hard with other kind of databases is to see how many unique users visited a given resource in a given amount of time. For instance I want to know the number of unique registered users, or IP addresses, that accessed a given article in an online newspaper.

# Why it matters
# Theory and technology base

## Pub/Sub
Do you know that Redis includes a fairly high performance implementation of Pub/Sub?

Redis Pub/Sub is very very simple to use, stable, and fast, with support for pattern matching, ability to subscribe/unsubscribe to channels on the run, and so forth. You can read more about it in the Redis PubSub official documentation. https://redis.io/docs/interact/pubsub/

## Queues
You probably already noticed how Redis commands like list push and list pop make it suitable to implement queues, but you can do more than that: Redis has blocking variants of list pop commands that will block if a list is empty. https://redis.io/commands/blpop/

A common usage of Redis as a queue is the Resque library, implemented and popularized by Github's folks.

With our http://redis.io/commands/rpoplpush list rotation commands it is possible to implement queues with interesting semantics that will make your background workers happier! (For instance you can implement a rotating list to fetch RSS feeds again and again, so that every worker will pick the RSS that was fetched more in the past, and thus needs to be updated ASAP). Similarly using sorted sets it is possible to implement priority queues easily.

## Caching
This section alone would deserve a specific blog post... so in short here I'll say that Redis can be used as a replacement for memcached in order to turn your cache into something able to store data in an simpler to update way, so that there is no need to regenerate the data every time. See for reference the first pattern published in this article.

# How to solve them
