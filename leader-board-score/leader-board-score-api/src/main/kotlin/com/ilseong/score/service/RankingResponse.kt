package com.ilseong.score.service

import com.ilseong.score.repository.PlayerRanking

data class RankingResponse(
    val count: Int,
    val rankingInfos: List<RankingInfo>
) {
    data class RankingInfo(
        val rank: Int,
        val player: String,
        val score: Int,
    )

    companion object {
        fun from(playerRankings: List<PlayerRanking>): RankingResponse {
            val rankingInfos = playerRankings.map { (ranking, player, score) -> RankingInfo(ranking, player, score) }
            return RankingResponse(playerRankings.count(), rankingInfos)
        }
    }
}