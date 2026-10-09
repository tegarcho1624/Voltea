package com.voltea.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.webkit.WebViewAssetLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    // Fondo animado de YouTube: la página consulta estos servidores (los mismos de AloTube).
    // La app hace esas peticiones por ella para evitar el bloqueo CORS del WebView.
    static final String APP_ORIGIN = "https://appassets.androidplatform.net";
    static final String UA = "Mozilla/5.0 (Linux; Android 13; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36";
    static final String[] SERVERS = {"pipedapi.kavin.rocks", "api.piped.private.coffee", "inv.nadeko.net", "yewtu.be"};

    static String header(WebResourceRequest r, String k) {
        for (Map.Entry<String, String> e : r.getRequestHeaders().entrySet()) {
            if (e.getKey().equalsIgnoreCase(k)) return e.getValue();
        }
        return null;
    }

    static WebResourceResponse proxy(WebResourceRequest r) {
        String host = r.getUrl().getHost();
        boolean ok = false;
        for (String h : SERVERS) {
            if (h.equals(host)) ok = true;
        }
        if (!ok) return null;
        // Solo las consultas de la API (no los videos), para que la reproducción siga su camino normal
        String path = r.getUrl().getPath();
        if (path == null || !(path.startsWith("/api/v1/") || path.startsWith("/streams/") || path.startsWith("/search"))) return null;

        String m = r.getMethod();
        Map<String, String> cors = new HashMap<>();
        cors.put("Access-Control-Allow-Origin", APP_ORIGIN);
        cors.put("Access-Control-Allow-Headers", "Range, Content-Type, Accept");
        cors.put("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS");
        if ("OPTIONS".equals(m)) {
            return new WebResourceResponse("text/plain", "utf-8", 204, "No Content", cors,
                    new ByteArrayInputStream(new byte[0]));
        }
        if (!"GET".equals(m) && !"HEAD".equals(m)) return null;

        try {
            String cur = r.getUrl().toString();
            for (int i = 0; i < 5; i++) {
                HttpURLConnection c = (HttpURLConnection) new URL(cur).openConnection();
                c.setInstanceFollowRedirects(false);
                c.setConnectTimeout(15000);
                c.setReadTimeout(30000);
                c.setRequestMethod(m);
                c.setRequestProperty("User-Agent", UA);
                c.setRequestProperty("Accept-Encoding", "identity");
                String acc = header(r, "Accept");
                if (acc != null) c.setRequestProperty("Accept", acc);
                int code = c.getResponseCode();
                String loc = c.getHeaderField("Location");
                if (code >= 300 && code < 400 && loc != null) {
                    cur = new URL(new URL(cur), loc).toString();
                    c.disconnect();
                    continue;
                }
                Map<String, String> h = new HashMap<>(cors);
                for (Map.Entry<String, List<String>> e : c.getHeaderFields().entrySet()) {
                    String k = e.getKey();
                    if (k == null) continue;
                    String lk = k.toLowerCase();
                    if (lk.startsWith("access-control-") || lk.equals("transfer-encoding")
                            || lk.equals("connection") || lk.equals("content-type")) continue;
                    StringBuilder sb = new StringBuilder();
                    for (String x : e.getValue()) {
                        if (sb.length() > 0) sb.append(", ");
                        sb.append(x);
                    }
                    h.put(k, sb.toString());
                }
                String mime = "application/json";
                String enc = null;
                String ct = c.getContentType();
                if (ct != null) {
                    String[] parts = ct.split(";");
                    mime = parts[0].trim();
                    for (String p : parts) {
                        String t = p.trim();
                        if (t.toLowerCase().startsWith("charset=")) enc = t.substring(8);
                    }
                }
                InputStream in = null;
                if (!"HEAD".equals(m)) in = code >= 400 ? c.getErrorStream() : c.getInputStream();
                if (in == null) in = new ByteArrayInputStream(new byte[0]);
                String reason = c.getResponseMessage();
                if (reason == null || reason.isEmpty()) reason = "OK";
                return new WebResourceResponse(mime, enc, code, reason, h, in);
            }
        } catch (Exception e) {
            // si falla, la página prueba con el siguiente servidor
        }
        return null;
    }
    private WebView web;
    private ValueCallback<Uri[]> chooser;
    private static final int REQ_FILE = 1;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);          // localStorage e IndexedDB
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(false);
        web.addJavascriptInterface(new Bridge(), "Android");

        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest r) {
                WebResourceResponse a = loader.shouldInterceptRequest(r.getUrl());
                if (a != null) return a;
                return proxy(r);
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView w, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (chooser != null) chooser.onReceiveValue(null);
                chooser = cb;
                try {
                    startActivityForResult(p.createIntent(), REQ_FILE);
                } catch (Exception e) {
                    chooser = null;
                    return false;
                }
                return true;
            }
        });
        web.loadUrl("https://appassets.androidplatform.net/assets/index.html");
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (req == REQ_FILE && chooser != null) {
            chooser.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res, data));
            chooser = null;
        } else {
            super.onActivityResult(req, res, data);
        }
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript("(window.voltBack&&window.voltBack())===true", v -> {
            if (!"true".equals(v)) finish();
        });
    }

    private void toast(final String m) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, m, Toast.LENGTH_LONG).show());
    }

    /** Puente para exportar la copia de seguridad a la carpeta Descargas */
    private class Bridge {
        @JavascriptInterface
        public void saveFile(String name, String b64, String mime) {
            try {
                byte[] data = Base64.decode(b64, Base64.DEFAULT);
                OutputStream os;
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues v = new ContentValues();
                    v.put(MediaStore.Downloads.DISPLAY_NAME, name);
                    v.put(MediaStore.Downloads.MIME_TYPE, mime);
                    v.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                    Uri u = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, v);
                    os = getContentResolver().openOutputStream(u);
                } else {
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    dir.mkdirs();
                    os = new FileOutputStream(new File(dir, name));
                }
                os.write(data);
                os.close();
                toast("Copia guardada en Descargas: " + name);
            } catch (Exception e) {
                toast("No se pudo guardar el archivo");
            }
        }
    }
}
