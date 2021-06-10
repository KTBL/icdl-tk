package de.ktbl.eikotiger.view.fragment

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import de.ktbl.eikotiger.data.Constants
import de.ktbl.eikotiger.data.db.AppDataBase
import de.ktbl.eikotiger.databinding.FragmentExportBinding
import de.ktbl.eikotiger.di.Injectable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * This fragment does not use any viewmodel since it has only a single functionality
 * and does not do any navigation or similar.
 */
class ExportFragment : Fragment(), Injectable {


    lateinit var binding: FragmentExportBinding
    private val isRunning = MutableLiveData(false)

    // Instead of using an Intent we use the new API with Contracts
    // OpenDocumentTree: asks to choose a directory and returns it as URI
    private val getExportFile = registerForActivityResult(
        ActivityResultContracts.CreateDocument(),
        this::onDirectoryPicked
    )

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        this::onPermissionRequested
    )

    // Well.. I don't like to use Suppress annotations,
    // but there isn't any other solution to this.
    // All potential blocking calls below are called
    // inside of the IO-Dispatcher context, and therefore
    // they should be fine.
    @Suppress("BlockingMethodInNonBlockingContext")
    private fun onDirectoryPicked(uri: Uri?) {
        if (uri == null) {
            return
        }
        lifecycleScope.launch(Dispatchers.Default) {
            isRunning.postValue(true)
            taggedTimber.v("Target export $uri")
            val dbPath = requireContext().getDatabasePath(AppDataBase.DB_NAME)
            taggedTimber.v("To be exported file: $dbPath")

            withContext(Dispatchers.IO) {
                try {
                    val contentResolver = requireContext().contentResolver
                    FileInputStream(dbPath).use { fin ->
                        contentResolver.openFileDescriptor(uri, "w")?.use { fd ->
                            FileOutputStream(fd.fileDescriptor).use { fout ->
                                var done = false
                                while (!done) {
                                    val byte = fin.read()
                                    if (byte == -1) {
                                        done = true
                                    } else {
                                        fout.write(byte)
                                    }
                                }
                            }

                        }
                    }
                } catch (exc: IOException) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            binding.root,
                            "Export gescheitert, bitte versuchen Sie es erneut.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

            }
            taggedTimber.v("Export done!")
            withContext(Dispatchers.Main) {
                Snackbar.make(
                    binding.root,
                    "Export erfolgreich",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            isRunning.postValue(false)
        }
    }

    private fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
        return if (permission == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            requestPermission.launch(WRITE_EXTERNAL_STORAGE)
            false
        }
    }

    private fun onPermissionRequested(granted: Boolean) {
        if (granted) {
            getExportFile.launch(getSuggestedExportName())
        } else {
            Snackbar.make(
                binding.root,
                "Zugriff auf das Dateisystem benötigt!",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExportBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.isRunning = isRunning
        binding.resultTxt.text = ""
        binding.startExportBtn.setOnClickListener {
            if (checkPermission()) {
                getExportFile.launch(getSuggestedExportName())
            }
        }
    }

    private fun getSuggestedExportName(): String {
        val format = SimpleDateFormat(
            "yyyyMMdd-HHmm_",
            Locale.forLanguageTag(
                Constants.LOCALE
            )
        )
        val dateTime = format.format(Date())
        return "${dateTime}_${AppDataBase.DB_NAME}"
    }

    companion object {
        val TAG = ExportFragment::class.simpleName
        val taggedTimber = Timber.tag(TAG)
        const val SQLITE_MIME = "application/x-sqlite3"

    }
}