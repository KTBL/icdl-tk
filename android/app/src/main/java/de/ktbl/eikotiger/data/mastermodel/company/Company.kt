package de.ktbl.eikotiger.data.mastermodel.company

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 * Currently also used for user identification
 * Default company owner = app owner = app user
 * https://developer.android.com/training/id-auth/identify
 */
@Entity(tableName = "company") //set to match tables
class Company {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    /**
     * User editable fields
     */
    //To be fully independent and flexible the "Betriebsnummer nach Viehverkehrsordndung" would have to be moved to Prod. Configuration
    //See https://www4.hi-tier.de/HitCom/hilfe/login.asp for simple overview
    //Caution: The 'Betriebs-ID' is not equal to the 'Betriebnummer' which is used for social insurance of staff
    //https://de.wikipedia.org/wiki/Betriebsnummer
    //The registry_key must not be published or tracked without consent, as it allows identification
    @ColumnInfo(name = "registry_key")
    var registryKey: String? = null

    @ColumnInfo(name = "company_name")
    var companyName: String? = null

    @ColumnInfo(name = "email")
    var email: String? = null

    @ColumnInfo(name = "note")
    var note: String? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "owner_name")
    var ownerName: String? = null

    @ColumnInfo(name = "phonenumber")
    var phone: String? = null

    //TODO: add collums for activeness and visibility states
    // TODO: if multi ids needed -relation from prododuction instance between companys
    //TODO: separate treatment of user and owner, for later user authentication (https://developer.android.com/training/id-auth)
    constructor()
    constructor(id: Long?, name: String?) {
        this.id = id
        companyName = name ?: ""
    }


    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

    companion object {
        //Place to enter defaults for testing on startup
        // returns List if objects that can be processed by insertAll
        @JvmStatic
        fun populateData(): Company {
            //supposed to add instances of company entity here
            return Company(1L, "Betrieb")
        }
    }
}