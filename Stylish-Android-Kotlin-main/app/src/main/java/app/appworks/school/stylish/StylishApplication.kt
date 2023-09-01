package app.appworks.school.stylish

import android.app.Application
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.util.ServiceLocator
import kotlin.properties.Delegates

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class StylishApplication : Application() {

    // Depends on the flavor,
    val stylishRepository: StylishRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: StylishApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
