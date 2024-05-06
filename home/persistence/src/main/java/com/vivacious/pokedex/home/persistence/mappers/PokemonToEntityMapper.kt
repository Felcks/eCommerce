package com.vivacious.pokedex.home.persistence.mappers

import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.home.persistence.models.PokemonEntity

fun Pokemon.toEntity() : PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        weight = weight,
        height = height,
        image = image,
    )
}