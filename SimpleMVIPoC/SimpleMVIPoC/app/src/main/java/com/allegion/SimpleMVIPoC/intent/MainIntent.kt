package com.allegion.SimpleMVIPoC.intent

/**
 * This class lists all possible "intents" possible within MainActivity
 * hence the naming scope of "MainIntent". A sealed class is a kotlin class
 * which allows subclassing only to occur within the scope of the class itself
 * which allows us to make a deterministic list of possible "intents" that can
 * occur
 */

sealed class MainIntent {
    // Think of this as a single "action" that can be performed by MainActivity
    // intents can also be implemented in a more generic manner, without the need
    // for them to be tied to a specific activity
    class RetrievePermissionIntent(val username: String, val password: String) : MainIntent()
}
