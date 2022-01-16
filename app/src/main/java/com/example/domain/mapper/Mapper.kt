package com.example.domain.mapper

interface Mapper<R, D> {
    fun mapFrom(inputModel: R): D
}