import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PdfViewer(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val encodedUrl = Uri.encode(url)
    val viewerUrl = "file:///android_asset/pdfjs/viewer.html?file=$encodedUrl"

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.allowFileAccess = true
                settings.allowUniversalAccessFromFileURLs = true  // Bu da önemli
                settings.domStorageEnabled = true
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                settings.setSupportZoom(true)
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                loadUrl(viewerUrl)
            }
        }
    )
}
