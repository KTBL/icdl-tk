package de.ktbl.eikotiger.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.ktbl.eikotiger.data.db.AppDataBase
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategoryDao
import de.ktbl.eikotiger.data.icdl.animalcategory.BranchDao
import de.ktbl.eikotiger.data.icdl.indicator.*
import de.ktbl.eikotiger.data.json.ICDLParser
import de.ktbl.eikotiger.data.json.ICDLParserRepository
import de.ktbl.eikotiger.data.mastermodel.company.CompanyDao
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceDao
import de.ktbl.eikotiger.data.mastermodel.location.LocationDao
import de.ktbl.eikotiger.data.mastermodel.stock.StockDao
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshotDao
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordDao
import de.ktbl.eikotiger.data.recordingmodel.record.OptionSelectionDao
import de.ktbl.eikotiger.data.recordingmodel.session.IndicatorRecordingSessionDao
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSessionDao
import javax.inject.Singleton

/**
 * NOTE: Defines application scope components for injection
 */
@Module(includes = [ViewModelModule::class, DataAccessModule::class])
internal class AppModule {
    @Singleton
    @Provides
    fun provideAssetManager(application: Application): AssetManager {
        return provideContext(application).assets
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    // --- PREFERNCES  ---
    @Provides
    @Singleton
    fun providePreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("DATA_STORE", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideICDLParser(
        assetManager: AssetManager?,
        icdlParserRepository: ICDLParserRepository?
    ): ICDLParser {
        return ICDLParser(assetManager!!, icdlParserRepository!!)
    }

    // --- DAO INJECTION ---
    @Singleton
    @Provides
    fun provideInstanceDao(db: AppDataBase): InstanceDao {
        return db.instanceDao()
    }

    @Singleton
    @Provides
    fun provideCompanyDao(db: AppDataBase): CompanyDao {
        return db.companyDao()
    }

    @Singleton
    @Provides
    fun provideLocationDao(db: AppDataBase): LocationDao {
        return db.locationDao()
    }

    @Singleton
    @Provides
    fun provideStockDao(db: AppDataBase): StockDao {
        return db.stockDao()
    }

    @Singleton
    @Provides
    fun provideAnimalCategoryDao(mb: AppDataBase): AnimalCategoryDao {
        return mb.animalCategoryDao()
    }

    @Singleton
    @Provides
    fun provideAssessmentDao(mb: AppDataBase): AssessmentDao {
        return mb.assessmentDao()
    }

    @Singleton
    @Provides
    fun provideIndicatorDao(mb: AppDataBase): IndicatorDao {
        return mb.indicatorDao()
    }

    @Singleton
    @Provides
    fun provideComposedVarDao(mb: AppDataBase): ComposedVarDao {
        return mb.composedVarDao()
    }

    @Singleton
    @Provides
    fun provideLocationSnapshotDao(mb: AppDataBase): LocationSnapshotDao {
        return mb.locationSnapshotDao()
    }

    @Singleton
    @Provides
    fun provideBranchDao(mb: AppDataBase): BranchDao {
        return mb.branchDao()
    }

    @Singleton
    @Provides
    fun provideGroupDao(mb: AppDataBase): GroupDao {
        return mb.groupDao()
    }

    @Singleton
    @Provides
    fun provideVarDao(mb: AppDataBase): VarDao {
        return mb.varDao()
    }

    @Singleton
    @Provides
    fun provideRecordingSessionDao(mb: AppDataBase): RecordingSessionDao {
        return mb.recordingSessionDao()
    }

    @Singleton
    @Provides
    fun provideIndicatorRecordDao(mb: AppDataBase): IndicatorRecordDao {
        return mb.indicatorRecordDao()
    }

    @Singleton
    @Provides
    fun provideIndicatorRecordingSessionDao(mb: AppDataBase): IndicatorRecordingSessionDao {
        return mb.indicatorRecordingSessionDao()
    }

    @Singleton
    @Provides
    fun provideOptionSelectionDao(mb: AppDataBase): OptionSelectionDao {
        return mb.optionSelectionDao()
    }
    // --- DATABASE INJECTION ---
    /**
     * Build the Database for actual User Input
     *
     *
     * This is currently also used for populating Table with defaults
     *
     *
     * Prefilling RoomDatabase with defaults is pretty easy from java code
     * However loading a database preset requires some more work
     * TODO: remove defaults when webservice/indicator description is ready
     * TODO: switch to more sophisticated migration (instead of destructive) for productive usage
     */
    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDataBase {
        val DB_NAME = "appdata.db"


        //TODO: urgently remove after development (this kills the database version but also it contents)
//        app.getApplicationContext()
//           .deleteDatabase(DB_NAME); //<<<< ADDED before building Database.
        return Room.databaseBuilder(app, AppDataBase::class.java, AppDataBase.DB_NAME)
            .fallbackToDestructiveMigration() //.allowMainThreadQueries()
            //.addMigrations(DataBase.mdbMigration)
            .build()
    }
}