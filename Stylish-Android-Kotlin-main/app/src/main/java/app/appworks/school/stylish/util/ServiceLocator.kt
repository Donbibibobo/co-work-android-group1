package app.appworks.school.stylish.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import app.appworks.school.stylish.data.source.DefaultStylishRepository
import app.appworks.school.stylish.data.source.StylishDataSource
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.data.source.local.StylishDatabase
import app.appworks.school.stylish.data.source.local.StylishLocalDataSource
import app.appworks.school.stylish.data.source.remote.StylishRemoteDataSource

/**
 * A Service Locator for the [StylishRepository].
 */
object ServiceLocator {

    @Volatile
    var stylishRepository: StylishRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): StylishRepository {
        synchronized(this) {
            return stylishRepository
                ?: stylishRepository
                ?: createStylishRepository(context)
        }
    }

    private fun createStylishRepository(context: Context): StylishRepository {
        return DefaultStylishRepository(
            StylishRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): StylishDataSource {
        return StylishLocalDataSource(StylishDatabase.getInstance(context).stylishDatabaseDao)
    }
}
