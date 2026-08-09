package dev.tuhkanens.comfortlib.update

sealed interface UpdateCheckResult {

    data class Result(
        val available: Boolean,
        val latest: String,
        val ahead: Boolean
    ) : UpdateCheckResult
    object Unavailable : UpdateCheckResult

}