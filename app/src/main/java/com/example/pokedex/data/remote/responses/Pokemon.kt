package com.example.pokedex.data.remote.responses

data class Pokemon (
    val abilities: List<Ability>,
    val baseExperience: Long,
    val cries: Cries,
    val forms: List<Species>,
    val gameIndices: List<GameIndex>,
    val height: Long,
    val heldItems: List<HeldItem>,
    val id: Long,
    val isDefault: Boolean,
    val locationAreaEncounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Long,
    val pastAbilities: List<Any?>,
    val pastTypes: List<Any?>,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Long
)

data class Ability (
    val ability: Species,
    val isHidden: Boolean,
    val slot: Long
)

data class Species (
    val name: String,
    val url: String
)

data class StatDetail (
    val name: String,
    val url: String
)

data class Cries (
    val latest: String,
    val legacy: String
)

data class GameIndex (
    val gameIndex: Long,
    val version: Species
)

data class HeldItem (
    val item: Species,
    val versionDetails: List<VersionDetail>
)

data class VersionDetail (
    val rarity: Long,
    val version: Species
)

data class Move (
    val move: Species,
    val versionGroupDetails: List<VersionGroupDetail>
)

data class VersionGroupDetail (
    val levelLearnedAt: Long,
    val moveLearnMethod: Species,
    val versionGroup: Species
)

data class GenerationV (
    val blackWhite: Sprites
)

data class GenerationIv (
    val diamondPearl: Sprites,
    val heartgoldSoulsilver: Sprites,
    val platinum: Sprites
)

data class Versions (
    val generationI: GenerationI,
    val generationIi: GenerationIi,
    val generationIii: GenerationIii,
    val generationIv: GenerationIv,
    val generationV: GenerationV,
    val generationVi: Map<String, Home>,
    val generationVii: GenerationVii,
    val generationViii: GenerationViii
)

data class Other (
    val dreamWorld: DreamWorld,
    val home: Home,
    val officialArtwork: OfficialArtwork,
    val showdown: Sprites
)

data class Sprites (
    val backDefault: String,
    val backFemale: Any? = null,
    val backShiny: String,
    val backShinyFemale: Any? = null,
    val front_default: String,
    val frontFemale: Any? = null,
    val frontShiny: String,
    val frontShinyFemale: Any? = null,
    val other: Other? = null,
    val versions: Versions? = null,
)

data class GenerationI (
    val redBlue: RedBlue,
    val yellow: RedBlue
)

data class RedBlue (
    val backDefault: String,
    val backGray: String,
    val backTransparent: String,
    val frontDefault: String,
    val frontGray: String,
    val frontTransparent: String
)

data class GenerationIi (
    val crystal: Crystal,
    val gold: Gold,
    val silver: Gold
)

data class Crystal (
    val backDefault: String,
    val backShiny: String,
    val backShinyTransparent: String,
    val backTransparent: String,
    val frontDefault: String,
    val frontShiny: String,
    val frontShinyTransparent: String,
    val frontTransparent: String
)

data class Gold (
    val backDefault: String,
    val backShiny: String,
    val frontDefault: String,
    val frontShiny: String,
    val frontTransparent: String? = null
)

data class GenerationIii (
    val emerald: OfficialArtwork,
    val fireredLeafgreen: Gold,
    val rubySapphire: Gold
)

data class OfficialArtwork (
    val frontDefault: String,
    val frontShiny: String
)

data class Home (
    val frontDefault: String,
    val frontFemale: Any? = null,
    val frontShiny: String,
    val frontShinyFemale: Any? = null
)

data class GenerationVii (
    val icons: DreamWorld,
    val ultraSunUltraMoon: Home
)

data class DreamWorld (
    val frontDefault: String,
    val frontFemale: Any? = null
)

data class GenerationViii (
    val icons: DreamWorld
)

data class Stat (
    val base_stat: Long,
    val effort: Long,
    val stat: StatDetail
)

data class Type (
    val slot: Long,
    val type: Species
)
