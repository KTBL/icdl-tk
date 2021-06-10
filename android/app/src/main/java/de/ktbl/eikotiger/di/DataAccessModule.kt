package de.ktbl.eikotiger.di

import dagger.Binds
import dagger.Module
import de.ktbl.eikotiger.data.apprepository.*
import de.ktbl.eikotiger.data.mastermodel.company.repository.CompanyRepository
import de.ktbl.eikotiger.view.datainterface.indicator.*
import de.ktbl.eikotiger.view.datainterface.productiondirection.IDirListBaseDA
import de.ktbl.eikotiger.view.datainterface.productiondirection.IProductionDirectionDetailsDA
import de.ktbl.eikotiger.view.datainterface.productiondirection.group.IGroupDetailsDA
import de.ktbl.eikotiger.view.datainterface.productiondirection.group.IGroupListDA
import de.ktbl.eikotiger.view.datainterface.recording.IChooseAnimalBranchDialogDA
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingEnvironmentDA
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingMasterDA
import de.ktbl.eikotiger.view.datainterface.settings.IBusinessInformationSettingsDA

@Suppress("unused")
@Module
abstract class DataAccessModule {

    @Binds
    abstract fun bindIBusinessInformationSettingsDA(companyRepository: CompanyRepository): IBusinessInformationSettingsDA

    @Binds
    abstract fun bindIDirListBaseDA(repository: AnimalCategoryListRepository): IDirListBaseDA

    @Binds
    abstract fun bindIProductionDirectionDetailsDA(repository: ProductionDirectionDetailsRepository): IProductionDirectionDetailsDA

    @Binds
    abstract fun bindIGroupListDA(repository: GroupListRepository): IGroupListDA

    @Binds
    abstract fun bindIGroupDetailsDA(repository: GroupDetailsRepository): IGroupDetailsDA

    @Binds
    abstract fun bindIIndicatorListDA(repository: IndicatorListRepository): IIndicatorListDA

    @Binds
    abstract fun bindIIndicatorInforDA(repository: IndicatorInfoRepository): IIndicatorInfoDA

    @Binds
    abstract fun bindIIndicatorOptionsOverviewDA(repository: IndicatorOptionsOverviewRepository): IIndicatorOptionsOverviewDA

    @Binds
    abstract fun bindIIndicatorOptionListBaseDA(repository: IndicatorOptionListBaseRepository): IIndicatorOptionListBaseDA

    @Binds
    abstract fun bindIIndicatorOptionDetailsDA(repository: IndicatorOptionDetailsRepository): IIndicatorOptionDetailsDA

    @Binds
    abstract fun bindIChooseAnimalBranchDialogDA(repository: ChooseAnimalBranchDialogRepository): IChooseAnimalBranchDialogDA

    @Binds
    abstract fun bindIRecordingEnvironmentDA(repository: RecordingEnvironmentRepository): IRecordingEnvironmentDA

    @Binds
    abstract fun binIRecordingMasterDA(repository: RecordingRepository): IRecordingMasterDA
}