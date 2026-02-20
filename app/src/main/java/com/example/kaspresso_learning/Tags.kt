package com.example.kaspresso_learning

/**
 * Теги для UI-элементов (testTag).
 * Используются в приложении и в тестах для идентификации элементов.
 */
object Tags {

    // Avatar Select Screen
    const val AVATAR_CONTAINER = "avatar_screen_container"
    const val AVATAR_ICON = "avatar_icon"                       // + "_$index"
    const val AVATAR_NEXT_BUTTON = "avatar_screen_next_button"
    const val AVATAR_ERROR = "avatar_screen_error_text"

    // Name Input Screen
    const val NAME_CONTAINER = "name_input_screen_container"
    const val NAME_INPUT_TITLE = "name_input_screen_title"
    const val NAME_INPUT_TEXT = "name_input_screen_edit_text"
    const val NAME_INPUT_LOGIN_BUTTON = "name_input_login_button"

    // Feed Screen
    const val FEED_CONTAINER = "feed_screen_container"
    const val FEED_TITLE = "feed_screen_title"
    const val FEED_POST = "feed_post"                           // + "_$index"
    const val FEED_LIST = "feed_screen_list"
    const val FEED_FAB = "feed_screen_fab"
    const val FEED_POST_AVATAR = "feed_post_avatar"
    const val FEED_POST_AUTHOR_NAME = "feed_post_author_name"
    const val FEED_POST_DATE = "feed_post_date"
    const val FEED_POST_TEA_NAME = "feed_post_tea_name"
    const val FEED_POST_TEA_TYPE = "feed_post_tea_type"
    const val FEED_POST_WEIGHT = "feed_post_weight_grams"
    const val FEED_POST_VOLUME = "feed_post_volume_ml"
    const val FEED_POST_VESSEL = "feed_post_vessel"
    const val FEED_POST_INFUSIONS = "feed_post_infusions"

    // New Brewing Screen
    const val BREWING_CONTAINER = "new_brewing_container"
    const val BREWING_SELECT_TEA = "new_brewing_select_tea"
    const val BREWING_SELECTED_TEA_NAME = "new_brewing_selected_tea_name"
    const val BREWING_WEIGHT = "new_brewing_weight"
    const val BREWING_VOLUME = "new_brewing_volume_ml"
    const val BREWING_VESSEL_GAIWAN = "new_brewing_vessel_gaiwan"
    const val BREWING_VESSEL_TEAPOT = "new_brewing_vessel_teapot"
    const val BREWING_SAVE = "new_brewing_save"
    const val BREWING_INFUSIONS = "new_brewing_infusions"
    const val BREWING_INFUSIONS_ITEM = "new_brewing_infusions_item" // + "_$n"

    // Tea Select Screen
    const val TEA_SELECT_CONTAINER = "tea_select_container"
    const val TEA_TYPE = "tea_type"                             // + "_$ordinal"
    const val TEA_ITEM = "tea_item"                             // + "_$id"

    // Profile Screen
    const val PROFILE_CONTAINER = "profile_container"
    const val PROFILE_NAME = "profile_name"
    const val PROFILE_COUNT = "profile_count"
    const val PROFILE_HISTORY_ITEM = "profile_history_item"     // + "_$index"
    const val PROFILE_LOGOUT = "profile_logout"
    const val PROFILE_EMPTY = "profile_empty_text"
}
