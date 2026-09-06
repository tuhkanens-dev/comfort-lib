package dev.tuhkanens.comfortlib.result;

public sealed interface UpdateResult permits
        UpdateResult.Result,
        UpdateResult.Unavailable {

    record Result(
            boolean available,
            String latest,
            boolean ahead
    ) implements UpdateResult {}

    enum Unavailable implements UpdateResult {
        INSTANCE
    }

}
