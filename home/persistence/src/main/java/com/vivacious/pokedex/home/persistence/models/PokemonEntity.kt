package com.vivacious.pokedex.home.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.vivacious.pokedex.domain.models.Ability
import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.domain.models.Status
import com.vivacious.pokedex.domain.models.Type

@Entity(tableName = "pokedex")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo(name = "id") override val id: Int,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "weight") override val weight: Int,
    @ColumnInfo(name = "height") override val height: Int,
    @ColumnInfo(name = "image") override val image: String
) : Pokemon {
    override val abilities: List<Ability>
        get() = listOf()
    override val types: List<Type>
        get() = listOf()
    override val status: List<Status>
        get() = listOf()

}