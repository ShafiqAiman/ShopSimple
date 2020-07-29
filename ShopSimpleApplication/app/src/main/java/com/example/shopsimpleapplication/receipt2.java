package com.example.shopsimpleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class receipt2 extends AppCompatActivity {

    Button btn_create_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt2);
        btn_create_pdf = (Button)findViewById(R.id.btn_create_pdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_create_pdf.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                createPDFFile(Common.getAppPath(receipt2.this)+"text_pdf.pdf");

                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    private void createPDFFile(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try{
            Document document = new Document();
            //Save
            //PdfWriter.getInstance(doc,new FileOutputStream());
            //Open to write
            document.open();

            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("ShopSimpleApplication");
            document.addCreator("ShopSimple");

            //Front Setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont(BaseFont.COURIER, "UTF-8",false);

            //Create Title of Doc
            Font titleFont = new Font(fontName,36.0f,Font.NORMAL, BaseColor.BLACK);
            addNewItem(document,"Order Details", Element.ALIGN_CENTER,titleFont);

            //Add more
            Font orderNumberFont = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,"Invoice No:",Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberValueFont = new Font(fontName,fontSize,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"10000",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document,"Date",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"29/7/2020",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document,"Cust PhoneNo",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"0182710535",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            //add product
            addLineSpace(document);
            addNewItem(document,"Items",Element.ALIGN_CENTER,titleFont);
            addLineSeperator(document);

            //Item 1




        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSeperator(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
}