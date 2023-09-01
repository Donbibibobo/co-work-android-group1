package app.appworks.school.stylish.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.factory.ViewModelFactory
import app.appworks.school.stylish.factory.ViewModelFactory2

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as StylishApplication).stylishRepository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}

fun Activity.getVmFactory2(): ViewModelFactory2 {
    val repository = (applicationContext as StylishApplication).stylishRepository
    val application = applicationContext as StylishApplication
    return ViewModelFactory2(repository, application)
}