import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PdfViewer(url: String) {
    var isReady by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        PdfRendererViewCompose(
            source = PdfSource.Remote(url),
            lifecycleOwner = LocalLifecycleOwner.current,
            modifier = Modifier.fillMaxSize(),
            onReady = {
                isReady = true
            }
        )

        if (!isReady) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}