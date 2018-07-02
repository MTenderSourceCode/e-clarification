package com.procurement.clarification.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.NotNull

data class ContactPoint @JsonCreator constructor(

        @field:NotNull
        val name: String,

        @field:NotNull
        val email: String,

        @field:NotNull
        val telephone: String,

        val faxNumber: String?,

        @field:NotNull
        val url: String
)