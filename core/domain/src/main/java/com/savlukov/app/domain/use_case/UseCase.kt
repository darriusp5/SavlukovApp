package com.savlukov.app.domain.use_case

abstract class UseCase<in P, out R> {
    abstract suspend operator fun invoke(params: P): R
}
