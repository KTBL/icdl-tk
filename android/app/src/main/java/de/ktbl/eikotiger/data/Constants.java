package de.ktbl.eikotiger.data;

import java.util.Locale;

public class Constants {

    //Set operation mode //TODO remove when new service works completely
   // public static final String DATASOURCE = "ICDL"; //SINGLE_ICDL, ICDL for single source, otherwise use two step download

    public static final String ICDL_FOLDER = "icdl";
    public static final String ICDL_PREFIX = "icdl";

    public static final String IMAGE_FOLDER = "images";
    public static final String IMAGE_FILE_EXTENSION = "jpg";


    //The date time format for string fomatting and parser
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //ISO 8601 w/o milliseconds
    //public static final String DATE_FORMAT = "yyyyMMdd"; //simple yyyyyMMdd
    //The locale/language string to determine prefix
    public static final String LOCALE = Locale.GERMANY.toLanguageTag();

    //Default entry id for single company
    public static final Long COMPANY_ID = 1L;
    public static final String COMPANY_UUID = "single-company-default";

    //Query String default
    public static final String DEFAULT_PARAM = " 1=1 ";
    public static final Long APLICATION_ID = 1L;

    public static int FRESH_TIMEOUT_IN_MINUTES = 1;

    //Key name for storing active production type in shared prefernces
    public static final String ACTIVE_KEY  = "ActiveInstance";
    //Key name for storing production types chosen to download/display in shared preferences
    public static final String CHOSEN_KEY  = "ChosenBranches";

}
