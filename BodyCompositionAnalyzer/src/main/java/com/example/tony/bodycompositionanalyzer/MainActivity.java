package com.example.tony.bodycompositionanalyzer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tony.bodycompositionanalyzer.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "PDFdemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public String createPdf() {
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000,
                PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72 / 1000, 1)
                .create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // 画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 写「Hello World」
        paint.setColor(Color.BLACK);
        page.getCanvas().drawText("Hello World!", 50, 50, paint);

        // 写「Hello World」
        paint.setColor(Color.BLACK);
        page.getCanvas().drawText("Hello World!", 20, 20, paint);

        // 写「√」
        paint.setColor(Color.BLACK);
        page.getCanvas().drawText("√", 100, 100, paint);


        // draw something on the page
        View content = findViewById(android.R.id.content);;
//       content.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        // add more pages
        // write the document content
        FileOutputStream os = null;
        String string = getExternalFilesDir(Environment.DIRECTORY_DCIM)
                + File.separator + "test.pdf";
        try {
            Log.i(LOG_TAG, "String:" + string);
            os = new FileOutputStream(string);
            document.writeTo(os);
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // close the document
            document.close();
        }

        return string;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parse_textview:
                try {
                    parseData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.textview:
                /* 创建PDF */
                String string = createPdf();
                /* 打开PDF */
                startActivity(getPdfFileIntent(string));
                break;
        }
    }

    private void parseData() throws IOException {
        Log.i(LOG_TAG, "parseData");
        InputStream in = getResources().getAssets().open("data.bin");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        byte[] bufferArray = buffer.toByteArray();

        /* 1. 判断回复是否正常 */
        byte[] ack = new byte[BodyComposition.ACK_LENGTH];
        System.arraycopy(bufferArray, BodyComposition.ACK_START, ack, 0, BodyComposition.ACK_LENGTH);
        if(Arrays.equals(ack, BodyComposition.ACK)) {
            /* 提取各个数据 */
            byte[] data2 = new byte[BodyComposition.DATA_LENGTH];
            System.arraycopy(bufferArray, BodyComposition.DATA_START, data2, 0, BodyComposition.DATA_LENGTH);
            /* 解析数据 */
            new BodyComposition(data2);
        }
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
