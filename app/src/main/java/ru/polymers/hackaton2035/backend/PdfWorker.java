package ru.polymers.hackaton2035.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PdfWorker {

    File file;
    int page_count;


    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PdfWorker(File file, Context context) throws IOException {
        this.file = file;

        PdfRenderer pr = new PdfRenderer(context.getSe);

        DecodeServiceBase decodeService = new DecodeServiceBase(new PdfContext());
        decodeService.setContentResolver(mContext.getContentResolver());

        decodeService.open(Uri.fromFile(file));
    }

    public Bitmap getPage(int num) {
        Bitmap dest = null;
        PdfRenderer.Page page =
        render(dest, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
    }*/
}
