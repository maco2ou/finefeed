package com.macoou.feed.operations

import com.macoou.feed.dataclasses.Article
import com.macoou.feed.dataclasses.Feed
import com.macoou.feed.dataclasses.FeedGroup

/**
 * Returns a list of all the articles from the feedGroup.
 *
 * @param feedGroup The feedGroup to get the list articles from.
 */
fun getAllArticles(feedGroup: FeedGroup): MutableList<Article> {
    val articles: MutableList<Article> = mutableListOf()

    for (feed in feedGroup.feeds) {
        for (article in feed.articles) {
            articles.add(article)
        }
    }

    return articles
}

/**
 * Returns a list of all bookmarked articles from the feedGroup.
 *
 * @param feed The feed to get the list articles from.
 */
fun getArticlesFromFeed(feed: Feed): MutableList<Article> {
    return feed.articles
}

/**
 * Returns a list of bookmarked articles from the feedGroup
 *
 * @param feedGroup The feedGroup to get the list of bookmarked articles from.
 */
fun getBookmarkedArticles(feedGroup: FeedGroup): MutableList<Article> {
    val bookmarkedArticles: MutableList<Article> = mutableListOf()

    for (feed in feedGroup.feeds) {
        for (article in feed.articles) {
            if (article.bookmarked) {
                bookmarkedArticles.add(article)
            }
        }
    }

    return bookmarkedArticles
}

/**
 * Returns a list of read articles from the feedGroup
 *
 * @param feedGroup The feedGroup to get the list of read articles from.
 */
fun getReadArticles(feedGroup: FeedGroup): MutableList<Article> {
    val readArticles: MutableList<Article> = mutableListOf()

    for (feed in feedGroup.feeds) {
        for (article in feed.articles) {
            if (article.read) {
                readArticles.add(article)
            }
        }
    }
    return readArticles
}
