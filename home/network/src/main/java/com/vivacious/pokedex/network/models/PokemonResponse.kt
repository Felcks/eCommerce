package com.vivacious.pokedex.network.models

import com.google.gson.annotations.SerializedName
import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.network.Constants.POKEMON_DETAIL_URL

class PokemonResponse(
    override val id: Int,
    override val name: String,
    override val weight: Int,
    override val height: Int,
    override val abilities: List<AbilityResponse>,
    override val types: List<TypeSlotResponse>,
    @SerializedName("stats")
    override val status: List<StatusResponse>,
    val sprites: SpriteResponse,
) : Pokemon {
    override val image: String
        get() = sprites.other.officialArtwork.frontDefault

    override val url: String
        get() = "${POKEMON_DETAIL_URL}$id/"
}