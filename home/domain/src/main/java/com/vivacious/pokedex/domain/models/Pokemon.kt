package com.vivacious.pokedex.domain.models

interface Pokemon : PokemonSummary {
    val id: Int
    override val name: String
    val weight: Int
    val height: Int
    override val image: String
    val abilities: List<Ability>
    val types: List<Type>
    val status: List<Status>
    override val url: String
}