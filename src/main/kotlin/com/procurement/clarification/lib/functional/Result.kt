package com.procurement.clarification.lib.functional

fun <T, E> T.asSuccess(): Result<T, E> = Result.success(this)
fun <T, E> E.asFailure(): Result<T, E> = Result.failure(this)

sealed class Result<out T, out E> {
    companion object {
        fun <T> pure(value: T) = Success(value)
        fun <T> success(value: T) = Success(value)
        fun <E> failure(error: E) = Failure(error)
        fun <T, E> merge(vararg results: Result<T, E>): Result<T, List<E>> {
            val errors = mutableListOf<E>()
                .apply {
                    for (result in results) {
                        when (result) {
                            is Success -> Unit
                            is Failure -> this.add(result.error)
                        }
                    }
                }
            return failure(errors)
        }
    }

    abstract val isSuccess: Boolean
    abstract val isFail: Boolean

    abstract val get: T
    abstract val error: E

    inline fun doOnError(block: (error: E) -> Unit): Result<T, E> {
        if (this.isFail) block(this.error)
        return this
    }

    inline fun onFailure(f: (Failure<E>) -> Nothing): T = when (this) {
        is Success<T> -> value
        is Failure<E> -> f(this)
    }

    val orNull: T?
        get() = when (this) {
            is Success -> get
            is Failure -> null
        }

    val asOption: Option<T>
        get() = when (this) {
            is Success -> Option.pure(get)
            is Failure -> Option.none()
        }

    infix fun <R : Exception> orThrow(block: (E) -> R): T = when (this) {
        is Success -> get
        is Failure -> throw block(this.error)
    }

    infix fun getOrElse(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> value
        is Failure -> defaultValue
    }

    infix fun orElse(defaultValue: () -> @UnsafeVariance T): T = when (this) {
        is Success -> value
        is Failure -> defaultValue()
    }

    infix fun <R> map(transform: (T) -> R): Result<R, E> = when (this) {
        is Success -> Success(transform(this.get))
        is Failure -> this
    }

    infix fun <R> mapFailure(transform: (E) -> R): Result<T, R> = when (this) {
        is Success -> this
        is Failure -> Failure(transform(this.error))
    }

    fun forEach(block: (T) -> Unit): Unit = when (this) {
        is Success -> block(value)
        is Failure -> Unit
    }

    class Success<out T> internal constructor(val value: T) : Result<T, Nothing>() {
        override val isSuccess: Boolean = true
        override val isFail: Boolean = false
        override val get: T = value
        override val error: Nothing
            get() = throw NoSuchElementException("The result does not contain an error.")

        override fun toString(): String = get.toString()
    }

    class Failure<out E> internal constructor(val reason: E) : Result<Nothing, E>() {
        override val isSuccess: Boolean = false
        override val isFail: Boolean = true
        override val get: Nothing
            get() = throw NoSuchElementException("The result does not contain a value.")
        override val error: E = reason

        override fun toString(): String = error.toString()
    }
}

infix fun <T, E> T.validate(rule: ValidationRule<T, E>): Result<T, E> {
    val result = rule.test(this)
    return when (result) {
        is ValidationResult.Ok -> Result.success(this)
        is ValidationResult.Fail -> Result.failure(result.error)
    }
}

infix fun <T, E> Result<T, E>.validate(rule: ValidationRule<T, E>): Result<T, E> = when (this) {
    is Result.Success -> {
        val result = rule.test(this.get)
        if (result.isError) Result.failure(
            result.error
        ) else Result.success(this.get)
    }
    is Result.Failure -> this
}

infix fun <T, R, E> Result<T, E>.flatMap(function: (T) -> Result<R, E>): Result<R, E> = when (this) {
    is Result.Success -> function(this.get)
    is Result.Failure -> this
}
