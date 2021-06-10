package de.ktbl.eikotiger.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory;
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategoryDao;
import de.ktbl.eikotiger.data.icdl.animalcategory.Branch;
import de.ktbl.eikotiger.data.icdl.animalcategory.BranchDao;
import de.ktbl.eikotiger.data.icdl.indicator.Assessment;
import de.ktbl.eikotiger.data.icdl.indicator.AssessmentDao;
import de.ktbl.eikotiger.data.icdl.indicator.ComposedVar;
import de.ktbl.eikotiger.data.icdl.indicator.ComposedVarDao;
import de.ktbl.eikotiger.data.icdl.indicator.Evaluation;
import de.ktbl.eikotiger.data.icdl.indicator.Group;
import de.ktbl.eikotiger.data.icdl.indicator.GroupDao;
import de.ktbl.eikotiger.data.icdl.indicator.Indicator;
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorBranchCrossRef;
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorDao;
import de.ktbl.eikotiger.data.icdl.indicator.Var;
import de.ktbl.eikotiger.data.icdl.indicator.VarDao;
import de.ktbl.eikotiger.data.mastermodel.company.Company;
import de.ktbl.eikotiger.data.mastermodel.company.CompanyDao;
import de.ktbl.eikotiger.data.mastermodel.instance.Instance;
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceDao;
import de.ktbl.eikotiger.data.mastermodel.location.Location;
import de.ktbl.eikotiger.data.mastermodel.location.LocationDao;
import de.ktbl.eikotiger.data.mastermodel.stock.Stock;
import de.ktbl.eikotiger.data.mastermodel.stock.StockDao;
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshot;
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshotDao;
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecord;
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordDao;
import de.ktbl.eikotiger.data.recordingmodel.record.OptionSelection;
import de.ktbl.eikotiger.data.recordingmodel.record.OptionSelectionDao;
import de.ktbl.eikotiger.data.recordingmodel.session.IndicatorRecordingSession;
import de.ktbl.eikotiger.data.recordingmodel.session.IndicatorRecordingSessionDao;
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSession;
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSessionDao;

/**
 * Kotlin encourages to export the schema for Development
 * see build.gradle - annotationProcessorOptions
 * TODO: Don't ship schemas to customer (add exportSchema = false)
 * <p>
 * MetaDatabase should only contain meta descriptors
 * so dropping would only affect versioning after all.
 */
@Database(
        entities = {
                Instance.class,
                Company.class,
                Location.class,
                Stock.class,
                AnimalCategory.class,
                Indicator.class,
                Assessment.class,
                Group.class,
                Branch.class,
                ComposedVar.class,
                Var.class,
                Evaluation.class,
                IndicatorBranchCrossRef.class,
                LocationSnapshot.class,
                IndicatorRecord.class,
                OptionSelection.class,
                IndicatorRecordingSession.class,
                RecordingSession.class
        },
        version = 1
)
public abstract class AppDataBase extends RoomDatabase {
    private static final int version = 1;

    public static final String DB_NAME = "appdata.db";

    private static AppDataBase INSTANCE;

    public abstract InstanceDao instanceDao();

    public abstract CompanyDao companyDao();

    public abstract LocationDao locationDao();

    public abstract StockDao stockDao();

    public abstract LocationSnapshotDao locationSnapshotDao();

    public abstract RecordingSessionDao recordingSessionDao();

    public abstract IndicatorRecordDao indicatorRecordDao();

    public abstract IndicatorRecordingSessionDao indicatorRecordingSessionDao();

    public abstract OptionSelectionDao optionSelectionDao();

    /**
     * Actually we can also just drop that for development versions
     * See .fallbackToDestructiveMigration().
     * TODO: Sophisticated Migrations for productive usage
     */
    public static final Migration mdbMigration = new Migration(version - 1, version) {
        @Override
        public void migrate(@NotNull SupportSQLiteDatabase database) {
            String TABLE_NAME_TEMP = "AppDataNew";
/*
 // 1. Create new table
 database.execSQL("CREATE TABLE IF NOT EXISTS ...")

 // 2. Copy the data
 database.execSQL("INSERT INTO $TABLE_NAME_TEMP ...")

 // 3. Remove the old table
 database.execSQL("DROP TABLE $TABLE_NAME")

 // 4. Change the table name to the correct one
 database.execSQL("ALTER TABLE $TABLE_NAME_TEMP RENAME TO $TABLE_NAME")
 */
        }
    };

    public abstract GroupDao groupDao();

    public abstract VarDao varDao();

    public abstract AnimalCategoryDao animalCategoryDao();

    public abstract BranchDao branchDao();

    public abstract IndicatorDao indicatorDao();

    public abstract AssessmentDao assessmentDao();

    public abstract ComposedVarDao composedVarDao();
}


