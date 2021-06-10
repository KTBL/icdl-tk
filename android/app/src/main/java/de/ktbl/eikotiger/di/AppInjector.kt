/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.ktbl.eikotiger.di

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import de.ktbl.eikotiger.EiKoTiGerApp
import timber.log.Timber

/**
 * Helper class to automatically inject fragments if they implement [Injectable].
 *
 * Notice when using Kotlin Annotation Processors in gradle need to be loaded via kapt
 * Generation of "DaggerAppComponent" will fail if DI is not setup in MainActivity and App name specified in Manifest
 *
 * register[Activity|Fragment]LifecycleCallbacks sets up listener that react to a state change
 */
object AppInjector {
    @JvmStatic
    fun init(eikotigerApp: EiKoTiGerApp) {
        DaggerAppComponent.builder().application(eikotigerApp)
                .build().inject(eikotigerApp)
        eikotigerApp
                .registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                        handleActivity(activity) //this distrubutes injects in activity and fragments
                        Timber.tag(activity.javaClass.simpleName).v("onCreate(Bundle)")
                    }

                    override fun onActivityDestroyed(activity: Activity) {
                        Timber.tag(activity.javaClass.simpleName).v("onDestroy()")
                    }

                    override fun onActivityPaused(activity: Activity) {
                        Timber.tag(activity.javaClass.simpleName).v("onPause()")
                    }

                    override fun onActivityResumed(activity: Activity) {
                        Timber.tag(activity.javaClass.simpleName).v("onResume()")
                    }

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                        Timber.tag(activity.javaClass.simpleName)
                            .v("onSaveInstanceState(Bundle)")
                    }

                    override fun onActivityStarted(activity: Activity) {
                        Timber.tag(activity.javaClass.simpleName).v("onStart()")
                    }

                    override fun onActivityStopped(activity: Activity) {
                        Timber.tag(activity.javaClass.simpleName).v("onStop()")
                    }
                })
    }

    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            Timber.tag(activity.javaClass.simpleName).v("Activity Injection")
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                            object : FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentActivityCreated(fm: FragmentManager,
                                                                       f: Fragment,
                                                                       savedInstanceState: Bundle?) {
                                    Timber.tag(f.javaClass.simpleName)
                                            .v("onActivityCreated()")
                                }

                                override fun onFragmentAttached(fm: FragmentManager, f: Fragment,
                                                                context: Context) {
                                    Timber.tag(f.javaClass.simpleName).v("onAttached()")
                                }

                                override fun onFragmentCreated(fm: FragmentManager, f: Fragment,
                                                               savedInstanceState: Bundle?) {
                                    Timber.tag(f.javaClass.simpleName).v("onCreated()")
                                    if (f is Injectable) {
                                        Timber.tag(f.javaClass.simpleName).v("Fragment Injection")
                                        AndroidSupportInjection.inject(f)
                                    }
                                }

                                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onDestroyed()")
                                }

                                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onDetached()")
                                }

                                override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onPaused()")
                                }

                                override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment,
                                                                   context: Context) {
                                    Timber.tag(f.javaClass.simpleName).v("onPreAttached()")
                                }

                                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onResumed()")
                                }

                                override fun onFragmentSaveInstanceState(fm: FragmentManager,
                                                                         f: Fragment,
                                                                         outState: Bundle) {
                                    Timber.tag(f.javaClass.simpleName)
                                            .v("onSaveInstanceState()")
                                }

                                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onStarted)")
                                }

                                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onStopped()")
                                }

                                override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment,
                                                                   v: View,
                                                                   savedInstanceState: Bundle?) {
                                    Timber.tag(f.javaClass.simpleName).v("onViewCreated()")
                                }

                                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                                    Timber.tag(f.javaClass.simpleName).v("onViewDestroyed()")
                                }
                            }, true)
        }
    }
}