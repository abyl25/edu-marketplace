package com.seniorproject.educationplatform.components;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpProcessor;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.User;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.repositories.UserRepo;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateGenerator {
    private CourseRepo courseRepo;
    private UserRepo userRepo;

    public CertificateGenerator(CourseRepo courseRepo, UserRepo userRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
    }

    public Map<String, String> generate(Long studentId, Long courseId, String outputFile) throws IOException, DocumentException {
        User student = userRepo.findById(studentId).get();
        String studentName = student.getFirstName() + " " + student.getLastName();
        Course course = courseRepo.findById(courseId).get();
        String courseName = course.getTitle();

        String basePath = "/home/abylay/IdeaProjects/education-platform/src/main/resources/files/certificates/";
        String input = basePath + "orig-cert.pdf";
        String filename = student.getId() + "-" + student.getFirstName() + "-" + student.getLastName() + "-" + "course-" + course.getId() + ".pdf";
        String output = basePath + filename;
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(output));

        Font fontTimesRoman_Italic_18 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.ITALIC, new BaseColor(87,81,80));
        Font fontTimesRoman_Italic_20 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.ITALIC, new BaseColor(87,81,80)); // 138,133,132
        Font fontTimesRoman_Italic_24 = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.ITALIC, new BaseColor(122, 33, 23));
        Font fontTimesRoman_Normal_40 = new Font(Font.FontFamily.TIMES_ROMAN, 40, Font.ITALIC, new BaseColor(122, 33, 23));

        Rectangle rect1 = new Rectangle(110,292,680,297); // clear dashed student name placeholder
        Rectangle rect2 = new Rectangle(290,348,500,372); // clear first text
        Rectangle rect3 = new Rectangle(330,90,470,113); // clear date
        List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>();
        cleanUpLocations.add(new PdfCleanUpLocation(1, rect1, BaseColor.WHITE));
        cleanUpLocations.add(new PdfCleanUpLocation(1, rect2, BaseColor.WHITE));
        cleanUpLocations.add(new PdfCleanUpLocation(1, rect3, new BaseColor(247, 245, 242)));

        PdfContentByte cb = stamper.getOverContent(1);

        ColumnText ct1 = new ColumnText(cb);
        ct1.setSimpleColumn(290, 340, 500, 365);
        Paragraph p1 = new Paragraph(new Phrase(0,"This certifies that", fontTimesRoman_Italic_18));
        p1.setAlignment(Element.ALIGN_CENTER);
        ct1.addElement(p1);
        ct1.go();

        ColumnText ct2 = new ColumnText(cb);
        ct2.setSimpleColumn(180, 280, 620, 320);
        Paragraph studentNameParagraph = new Paragraph(new Phrase(0, studentName, fontTimesRoman_Normal_40));
        studentNameParagraph.setAlignment(Element.ALIGN_CENTER);
        ct2.addElement(studentNameParagraph);
        ct2.go();

        ColumnText ct3 = new ColumnText(cb);
        ct3.setSimpleColumn(95, 242, 705, 285);
        Paragraph p2 = new Paragraph(new Phrase(0,"has successfully completed course of ", fontTimesRoman_Italic_18));
        p2.setAlignment(Element.ALIGN_CENTER);
        ct3.addElement(p2);
        ct3.go();

        ColumnText ct4 = new ColumnText(cb);
        ct4.setSimpleColumn(95, 222, 705, 248);
        Paragraph courseNameParagraph = new Paragraph(new Phrase(0, courseName, fontTimesRoman_Italic_24));
        courseNameParagraph.setAlignment(Element.ALIGN_CENTER);
        ct4.addElement(courseNameParagraph);
        ct4.go();

        ColumnText ct5 = new ColumnText(cb);
        ct5.setSimpleColumn(330, 80, 470, 94);
        ZonedDateTime zdt = ZonedDateTime.now();
        String month = zdt.getMonth().toString().substring(0, 1) + zdt.getMonth().toString().substring(1).toLowerCase();
        String curDate = zdt.getDayOfMonth() + " " + month + " " + zdt.getYear();
        Paragraph dateParagraph = new Paragraph(new Phrase(0, curDate, fontTimesRoman_Italic_20));
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        ct5.addElement(dateParagraph);
        ct5.go();

        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(cleanUpLocations, stamper);
        cleaner.cleanUp();
        stamper.close();
        reader.close();

        Map<String, String> response = new HashMap<>();
        response.put("basePath", basePath);
        response.put("filename", filename);
        return response;
    }
}
