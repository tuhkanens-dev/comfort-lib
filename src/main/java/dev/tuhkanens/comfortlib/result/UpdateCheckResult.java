package dev.tuhkanens.comfortlib.result;

public sealed interface UpdateCheckResult {

    record Result(
            boolean available,
            String latest,
            boolean ahead
    ) implements UpdateCheckResult {}

    enum Unavailable implements UpdateCheckResult {
        INSTANCE
    }

}
