package com.allegion.SimpleMVIPoC.model.data

/**
 * A class representing possible permissions a logged-in user can have
 * assigned to them
 */
enum class Permission {
    READ_PERMISSION,
    WRITE_PERMISSION,
    VIEW_USER,
    CREATE_USER,
    DELETE_USER
}