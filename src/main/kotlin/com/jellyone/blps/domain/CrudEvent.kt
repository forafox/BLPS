package com.jellyone.blps.domain


import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "operation"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = CreateEvent::class, name = "CREATE"),
    JsonSubTypes.Type(value = UpdateEvent::class, name = "UPDATE"),
    JsonSubTypes.Type(value = DeleteEvent::class, name = "DELETE")
)
sealed class CrudEvent

data class CreateEvent(
    val payload: Accommodation
) : CrudEvent()

data class UpdateEvent(
    val payload: Accommodation
) : CrudEvent()

data class DeleteEvent(
    val payload: Long
) : CrudEvent()