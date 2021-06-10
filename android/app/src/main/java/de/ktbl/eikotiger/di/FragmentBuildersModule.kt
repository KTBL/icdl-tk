package de.ktbl.eikotiger.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.ktbl.eikotiger.view.fragment.ExportFragment
import de.ktbl.eikotiger.view.fragment.FullscreenImageDialog
import de.ktbl.eikotiger.view.fragment.home.SplashScreenFragment
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorInfoFragment
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorListFragment
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionDetailsFragment
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionListFragment
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionDetailFragment
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionsOverviewFragment
import de.ktbl.eikotiger.view.fragment.productiondirection.group.GroupDetailFragment
import de.ktbl.eikotiger.view.fragment.productiondirection.group.GroupListFragment
import de.ktbl.eikotiger.view.fragment.recording.ChooseAnimalGroupDialog
import de.ktbl.eikotiger.view.fragment.recording.RecordingOverviewFragment
import de.ktbl.eikotiger.view.fragment.recording.setup.ChooseAnimalCategoryBranchDialog
import de.ktbl.eikotiger.view.fragment.recording.setup.RecordingSetupFragment
import de.ktbl.eikotiger.view.fragment.recording.setup.RecordingSetupMenuFragment
import de.ktbl.eikotiger.view.fragment.settings.BusinessInfoFragment

/** NOTE: BuildersModule class which will define bindings for our sub-components so that Dagger can inject them.
 */
@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun productionDirectionsOverviewFragment(): ProductionDirectionsOverviewFragment?

    @ContributesAndroidInjector
    abstract fun productionDirectionDetailFragment(): ProductionDirectionDetailFragment?

    @ContributesAndroidInjector
    abstract fun splashScreeFragment(): SplashScreenFragment?

    @ContributesAndroidInjector
    abstract fun indicatorListFragment(): IndicatorListFragment?

    @ContributesAndroidInjector
    abstract fun groupListFragment(): GroupListFragment?

    @ContributesAndroidInjector
    abstract fun groupDetailFragment(): GroupDetailFragment?

    @ContributesAndroidInjector
    abstract fun businessInfoFragment(): BusinessInfoFragment?

    @ContributesAndroidInjector
    abstract fun indicatorInfoFragment(): IndicatorInfoFragment?

    @ContributesAndroidInjector
    abstract fun classificationOptionListFragment(): IndicatorOptionListFragment?

    @ContributesAndroidInjector
    abstract fun indicatorOptionDetailsFragment(): IndicatorOptionDetailsFragment?

    @ContributesAndroidInjector
    abstract fun recordingSetupFragment(): RecordingSetupFragment?

    @ContributesAndroidInjector
    abstract fun recordingSetupMenuFragment(): RecordingSetupMenuFragment?

    @ContributesAndroidInjector
    abstract fun chooseAnimalCategoryBranchDialog(): ChooseAnimalCategoryBranchDialog?

    @ContributesAndroidInjector
    abstract fun fullscreenImageDialog(): FullscreenImageDialog?

    @ContributesAndroidInjector
    abstract fun recordingOverviewFragment(): RecordingOverviewFragment?

    @ContributesAndroidInjector
    abstract fun chooseAnimalGroupDialog(): ChooseAnimalGroupDialog?

    @ContributesAndroidInjector
    abstract fun exportFragment(): ExportFragment?
}