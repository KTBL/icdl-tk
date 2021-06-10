package de.ktbl.eikotiger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import de.ktbl.eikotiger.view.viewmodel.AppViewModelFactory
import de.ktbl.eikotiger.view.viewmodel.ImageDialogVM
import de.ktbl.eikotiger.view.viewmodel.RecordingStateVM
import de.ktbl.eikotiger.view.viewmodel.home.SplashscreenVM
import de.ktbl.eikotiger.view.viewmodel.indicator.*
import de.ktbl.eikotiger.view.viewmodel.productiondirection.*
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupDetailVM
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListItem
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListVM
import de.ktbl.eikotiger.view.viewmodel.recording.ChooseAnimalGroupVM
import de.ktbl.eikotiger.view.viewmodel.recording.ClassificationInputVM
import de.ktbl.eikotiger.view.viewmodel.recording.RecordingOverviewVM
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
import de.ktbl.eikotiger.view.viewmodel.recording.setup.ChooseAnimalBranchDialogVM
import de.ktbl.eikotiger.view.viewmodel.recording.setup.RecordingMenuVM
import de.ktbl.eikotiger.view.viewmodel.recording.setup.RecordingSetupVM
import de.ktbl.eikotiger.view.viewmodel.settings.BusinessInformationSettingsVM

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(IndicatorListVM::class)
    abstract fun bindIndicatorListVM(indicatorListVM: IndicatorListVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(IndicatorListItem::class)
    abstract fun bindIndicatorListItem(indicatorListItem: IndicatorListVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ClassificationOptionVM::class)
    abstract fun bindClassificationOptiontOptionVM(classificationOptionVM: ClassificationOptionVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(GroupListVM::class)
    abstract fun bindGroupListVM(groupListVM: GroupListVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(GroupListItem::class)
    abstract fun bindgroupListItem(groupListItemVM: GroupListItem?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(GroupDetailVM::class)
    abstract fun bindGroupDetailVM(groupDetailVM: GroupDetailVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SplashscreenVM::class)
    abstract fun bindSplashscreenVM(splashscreenVM: SplashscreenVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductionDirectionDetailsVM::class)
    abstract fun bindProductionDirectionDetailsVM(productionDirectionDetailsVM: ProductionDirectionDetailsVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductionDirectionListItem::class)
    abstract fun bindProductionDirectionListItem(productionDirectionListItem: ProductionDirectionListItem?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductionDirectionSectionVM::class)
    abstract fun bindProductionDirectionSectionVM(productionDirectionSectionVM: ProductionDirectionSectionVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BusinessInformationSettingsVM::class)
    abstract fun bindBusinessInformationSettingsVM(businessInformationSettingsVM: BusinessInformationSettingsVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(IndicatorInfoVM::class)
    abstract fun bindIndicatorInfoVM(indicatorInfoVM: IndicatorInfoVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ClassificationInputVM::class)
    abstract fun bindClassificationInputVM(classificationInputVM: ClassificationInputVM): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(IndicatorOptionsOverviewVM::class)
    abstract fun bindIndicatorOptionsOverviewVM(indicatorOptionsOverviewVM: IndicatorOptionsOverviewVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(IndicatorOptionDetailsVM::class)
    abstract fun bindIndicatorOptionDetailsVM(indicatorOptionDetailsVM: IndicatorOptionDetailsVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RecordingSetupVM::class)
    abstract fun bindRecordingSetupVM(recordingSetupVM: RecordingSetupVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RecordingMenuVM::class)
    abstract fun bindRecordingMenuVM(recordingMenuVM: RecordingMenuVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RecordingEnvironment::class)
    abstract fun bindSharedRecordingSetupVM(recordingEnvironment: RecordingEnvironment?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(DirListModeVM::class)
    abstract fun bindDirListModeVM(dirListModeVM: DirListModeVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(DirAddingModeVM::class)
    abstract fun bindDirAddingModeVM(dirAddingModeVM: DirAddingModeVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(DirSelectionModeVM::class)
    abstract fun bindDirSelectionModeVM(dirSelectionModeVM: DirSelectionModeVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RecordingStateVM::class)
    abstract fun bindMainStateVM(recordingStateVM: RecordingStateVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ImageDialogVM::class)
    abstract fun bindImageDialogVM(imageDialogVM: ImageDialogVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAnimalBranchDialogVM::class)
    abstract fun bindChooseAnimalBranchDialogVM(chooseAnimalBranchDialogVM: ChooseAnimalBranchDialogVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RecordingOverviewVM::class)
    abstract fun bindRecordingOverViewVM(recordingOverviewVM: RecordingOverviewVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseAnimalGroupVM::class)
    abstract fun bindChooseAnimalGroupVM(chooseAnimalGroupVM: ChooseAnimalGroupVM): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory?): ViewModelProvider.Factory?
}