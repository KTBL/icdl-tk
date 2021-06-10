package de.ktbl.eikotiger.view.viewmodel;

import android.widget.EditText;

import androidx.databinding.InverseMethod;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.ktbl.eikotiger.R;
import timber.log.Timber;


/**
 * Converter Collection class
 */
public class Converter {
    private final static String TAG = Converter.class.getSimpleName();

    private static void setIllegalValueError(EditText view) {
        String msg = view.getContext()
                         .getString(R.string.illegal_value);
        view.setError(msg);
    }

    // Contract annotation simply says: "If the value is null, an exception will be thrown
    // This way the static analysis of intellij (or in this case android studio) knows,
    // if this function returns without having thrown an exception num != null
    // Therefore it does not highlight code which is executed after this function
    // has been called must not be marked with a "num is maybe null" highlight.
    @Contract("null -> fail")
    private static void throwParseExceptionIfNull(Number num) throws ParseException {
        if (num == null) {
            // this should never ever ever ever happen,
            // since it should throw an parseException
            // if it can not parse it, instead of returning
            // null
            String msg =
                    "Somehow num is null. The parsing failed and returned null instead of throwing a ParsingException";
            Timber.tag(TAG)
                  .wtf(msg);
            throw new ParseException(msg, -1);
        }
    }

    @InverseMethod("stringToInteger")
    public static String integerToString(EditText view, Integer oldValue, Integer value) {
        return value == null ? "" : value.toString();
    }

    public static Integer stringToInteger(EditText view, Integer oldValue, String newValue) {
        if (newValue == null) {
            return 0;
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntegerOnly(true);
        try {
            Number numValue = nf.parse(newValue);
            Converter.throwParseExceptionIfNull(numValue);
            int intValue = numValue.intValue();
            if (intValue < 0) {
                Converter.setIllegalValueError(view);
            } else {
                view.setError(null);
            }
            // It would be possible, that we return an negative value,
            // this is valid since we show the user an error message
            // about the illegal value.
            // So, this converter is for usability reasons, not for
            // value range enforcment.
            // Ideally this converter will be used for input fields which
            // have inputtype number, not inputtype signed_number - but
            // still we handle this case as well.
            return intValue;
        } catch (ParseException parseException) {
            Timber.tag(TAG)
                  .d(parseException,
                     "Exception while parsing what to be expected and unsigned int: %s",
                     newValue);
            Converter.setIllegalValueError(view);
            return null;
        }
    }

    @InverseMethod("stringToDouble")
    public static String doubleToString(EditText view, Double oldValue, Double value) {
        // oldValue argument of this function does always hold the same as the value
        // argument. This happens due to the way android treats such converter functions
        // https://medium.com/androiddevelopers/android-data-binding-inverse-functions-95aab4b11873
        // while this really sucks, it at least enables us to parse the real old value
        // by querying the EditText and parse its content
        // Since at this point oldValue.equals(value) is always true we first have to
        // set it to null, this way the operations below will work like intended
        oldValue = null;
        try {
            String text = view.getText()
                              .toString();
            oldValue = Double.parseDouble(text);
        } catch (NumberFormatException numberFormatException) {
            Timber.tag(TAG)
                  .d(numberFormatException,
                     "Could not parse a double from %s",
                     view.getText()
                         .toString());
        }

        if (oldValue != null && oldValue.equals(value)) {
            return view.getText()
                       .toString();
        } else if (value == null) {
            return "";
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);
            return nf.format(value);
        }
    }

    public static Double stringToDouble(EditText view, Double oldValue, String newValue) {
        if (newValue == null) {
            return 0d;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            Number parsedNum = nf.parse(newValue);
            Converter.throwParseExceptionIfNull(parsedNum);
            // if any error has been set, unset it, since parsing the value
            // has been successful
            view.setError(null);
            return parsedNum.doubleValue();
        } catch (ParseException parseException) {
            Timber.tag(TAG)
                  .d(parseException, "Could not parse Double from string: %s", newValue);
            Converter.setIllegalValueError(view);
            return null;
        }
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
