package com.mywork.apps.cryptodotcom.challenge.utilities

import com.mywork.apps.cryptodotcom.challenge.R


/**
 * Constants used throughout the app.
 */
const val DATABASE_NAME = "currency-db"
const val DATA_FILENAME = "full_list.json"
const val DATA_EXPIRED_INTERVAL = 1800000


object MessageRes {
    const val UNKNOWN_ERROR_RES = R.string.unknown_error
    const val DATA_WAS_LOADED_RES = R.string.data_was_loaded
    const val DATA_LOADER_RUNNING_RES = R.string.data_loader_running
}
