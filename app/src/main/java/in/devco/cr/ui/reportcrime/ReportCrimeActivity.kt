package `in`.devco.cr.ui.reportcrime

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.extensions.hide
import `in`.devco.cr.extensions.loadUri
import `in`.devco.cr.extensions.show
import `in`.devco.cr.util.AppUtils.getRealPathFromURI
import `in`.devco.cr.util.LocationListener
import `in`.devco.cr.util.checkLocationPermissions
import `in`.devco.cr.util.displayDialog
import `in`.devco.cr.util.getLocation
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Location
import android.net.Uri
import android.widget.Toast
import butterknife.OnClick
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_report_crime.*
import kotlinx.android.synthetic.main.button_action.*

class ReportCrimeActivity : BaseMVVMActivity<Boolean, ReportCrimeViewModel>(), LocationListener {
    companion object {
        private const val REQUEST_CODE_IMAGE = 901
        private const val REQUEST_CODE_VIDEO = 902

        fun launch(context: Context) {
            context.startActivity(Intent(context, ReportCrimeActivity::class.java))
        }
    }

    private val disposable = CompositeDisposable()
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    override fun layoutRes() = R.layout.activity_report_crime

    override fun init() {
        super.init()
        actionTextView.setText(R.string.report)
        isDisplayHomeAsUpEnabled = true

        checkPermission()
    }

    private fun checkPermission() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.setLogging(true)

        disposable.clear()
        disposable.add(
            rxPermissions
                .requestEachCombined(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe { permission ->
                    when {
                        permission.granted -> {
                        }
                        else -> displayDialog(this)
                    }
                }
        )
    }

    override fun setData(data: Boolean) {
        if (data) {
            Toast.makeText(this, "Report submitted", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    @OnClick(R.id.addImageButton)
    fun addImage() {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .maxSelectable(1)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.Matisse_Dracula)
            .forResult(REQUEST_CODE_IMAGE)
    }

    @OnClick(R.id.addVideoButton)
    fun addVideo() {
        Matisse.from(this)
            .choose(MimeType.ofVideo())
            .maxSelectable(1)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.Matisse_Dracula)
            .forResult(REQUEST_CODE_VIDEO)
    }

    @OnClick(R.id.removeImageButton)
    fun removeImage() {
        imageUri = null
        addImageButton.setImageResource(R.drawable.ic_filter_black_24dp)
        removeImageButton.hide()
    }

    @OnClick(R.id.removeVideoButton)
    fun removeVideo() {
        videoUri = null
        removeVideoButton.hide()
    }

    @OnClick(R.id.actionButton)
    fun report() {
        checkLocationPermissions(this, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    imageUri = Matisse.obtainResult(data)[0]
                    addImageButton.loadUri(imageUri)
                    removeImageButton.show()
                }
                REQUEST_CODE_VIDEO -> {
                    videoUri = Matisse.obtainResult(data)[0]
                    removeVideoButton.show()
                }
            }
        }
    }

    override fun onPermissionGranted() {
        getLocation(this, this)
    }

    override fun onLocationFound(location: Location) {
        viewModel.reportCrime(
            descriptionEditText.text.toString(),
            location,
            getRealPathFromURI(imageUri, this),
            getRealPathFromURI(imageUri, this)
        )
    }

    override fun onLocationNotFound() {
    }
}
