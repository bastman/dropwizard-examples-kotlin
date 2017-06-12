package com.example.demo.jdbi.tweet

import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.tweak.ResultSetMapper
import ru.vyarus.guicey.jdbi.installer.repository.JdbiRepository
import ru.vyarus.guicey.jdbi.tx.InTransaction
import java.sql.ResultSet
import java.time.Instant

@JdbiRepository
@InTransaction
@RegisterMapper(TweetMapper::class)
interface TweetRepository {
    @GetGeneratedKeys

    @SqlUpdate("INSERT INTO tweets (tweetId, message, modified_at) VALUES (null, :message" +
        ", UTC_TIMESTAMP(3))")
        /*
@SqlUpdate("INSERT INTO tweets (tweetId, message, modifiedAt) VALUES (:tweetId, :message" +
        ", :modifiedAt)")
        */
    fun add(@BindBean tweet: TweetAddRequest): Int

    @SqlUpdate("update tweets set message=:message, modifiedAt=:modifiedAt where tweetId=:tweetId")
    fun update(@BindBean tweet: Tweet): Int

    @SqlQuery("select * from tweets where tweetId = :tweetId")
    fun findById(@Bind("tweetId") tweetId: Int): Tweet

    @SqlQuery("select * from tweets")
    fun findAll(): List<Tweet>
}

data class TweetAddRequest(val modifiedAt: Instant, val message: String)

class TweetMapper : ResultSetMapper<Tweet> {

    override fun map(index: Int, r: ResultSet, ctx: StatementContext?): Tweet {
        return Tweet(tweetId = r.getInt("tweetId"), message = r.getString("message"), modifiedAt = r.getTimestamp("modified_at").toInstant())
    }
}

data class Tweet(
    val tweetId: Int,
    val message: String,
    val modifiedAt: Instant
)
