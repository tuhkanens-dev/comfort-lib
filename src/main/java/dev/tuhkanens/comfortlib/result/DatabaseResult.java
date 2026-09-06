package dev.tuhkanens.comfortlib.result;

public sealed interface DatabaseResult<T> permits
    DatabaseResult.Result,
    DatabaseResult.Failed,
    DatabaseResult.Already,
    DatabaseResult.Success,
    DatabaseResult.NotFound {

    record Result<T>(
            T result
    ) implements DatabaseResult<T> {}

    record Failed(
            String message
    ) implements DatabaseResult<Void> {}

    enum Already implements DatabaseResult<Void> {
        INSTANCE
    }

    enum Success implements DatabaseResult<Void> {
        INSTANCE
    }

    enum NotFound implements DatabaseResult<Void> {
        INSTANCE
    }

}