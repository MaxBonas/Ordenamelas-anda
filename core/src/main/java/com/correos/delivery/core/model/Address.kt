package com.correos.delivery.core.model

/**
 * Data model representing a postal address and its coordinates.
 */
data class Address(
    val postalCode: String,
    val street: String,
    val number: String,
    val latitude: Double,
    val longitude: Double
)
