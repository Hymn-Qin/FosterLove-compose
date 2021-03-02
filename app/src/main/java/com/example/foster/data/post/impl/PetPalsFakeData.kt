package com.example.foster.data.post.impl

import com.example.foster.model.PetPal

val pal1 = PetPal(
    id = "11aa123ada",
    name = "Pol"
)

val pal2 = PetPal(
    id = "22aa123ada",
    name = "Pol"
)

val pal3 = PetPal(
    id = "33aa123ada",
    name = "Pol"
)

val pal4 = PetPal(
    id = "44aa123ada",
    name = "Pol"
)

val pal5 = PetPal(
    id = "55aa123ada",
    name = "Pol"
)

val PET_PALS: List<PetPal> =
    listOf(
        pal1,
        pal2,
        pal3,
        pal4,
        pal5,
        pal1.copy(id = "66aa123ada", name = "Pol"),
        pal2.copy(id = "77aa123ada", name = "Pol"),
        pal3.copy(id = "88aa123ada", name = "Pol"),
        pal4.copy(id = "99aa123ada", name = "Pol"),
        pal5.copy(id = "12aa123ada", name = "Pol"),
    )