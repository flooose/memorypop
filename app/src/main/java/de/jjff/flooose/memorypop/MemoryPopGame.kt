package de.jjff.flooose.memorypop

import de.jjff.flooose.memorypop.daggerstuff.ActivityComponent
import de.jjff.flooose.memorypop.daggerstuff.ActivityModule
//import de.jjff.flooose.memorypop.daggerstuff.DaggerActivityComponent
import de.jjff.flooose.memorypop.services.DataService
import javax.inject.Inject


class MemoryPopGame {
    @Inject
    internal var firebaseDataService: DataService? = null

    var component: ActivityComponent? = null

    constructor() {
        //component = DaggerActivityComponent.builder().activityModule(ActivityModule(this)).build()
        //component.inject(this)
    }

    fun createEntry(word: String, definition: String): Unit {
    }

    fun updateEntry(definition: String): Unit {

    }

    fun nextRandomWord() {}

    fun getCurrentWord(): String {
        return null as String
    }

    fun getCurrentDefinition(): String {
        return null as String
    }
}