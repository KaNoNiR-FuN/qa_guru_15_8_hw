import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Champion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesTests {

    ClassLoader cl = FilesTests.class.getClassLoader();

    @DisplayName("Test json with Jackson")
    @Test
    void jsonTest() throws Exception {
        File file = new File("src/test/resources/Champion.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Champion champion = objectMapper.readValue(file, Champion.class);

        assertThat(champion.name).isEqualTo("Vi");
        assertThat(champion.type).isEqualTo("Diver");
        assertThat(champion.price).isEqualTo(4800);
        assertThat(champion.skills.get(4)).isEqualTo("Cease and Desist");
        assertThat(champion.stats.health).isEqualTo(2338);
        assertThat(champion.stats.armor).isEqualTo(109);
        assertThat(champion.stats.damage).isEqualTo(114);
    }

    @DisplayName("Test PDF from Zip")
    @Test
    void pdfTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/TestFiles.zip"));
        ZipInputStream zipInputStream = new ZipInputStream(cl.getResourceAsStream("TestFiles.zip"));
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.getName().contains("pdfExample.pdf")) {
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    assertThat(pdf.text).contains("This is a small demonstration .pdf file");
                }
            }
        }
    }

    @DisplayName("Test XLS from Zip")
    @Test
    void xlsTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/TestFiles.zip"));
        ZipInputStream zipInputStream = new ZipInputStream(cl.getResourceAsStream("TestFiles.zip"));
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.getName().contains("xlsExample.xls")) {
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    XLS xls = new XLS(inputStream);
                    assertThat(
                            xls.excel.getSheetAt(0)
                                    .getRow(9)
                                    .getCell(2)
                                    .getStringCellValue()
                    ).isEqualTo("Weiland");
                }
            }
        }
    }

    @DisplayName("Test CSV from Zip")
    @Test
    void csvTest() throws Exception {
        ZipFile zipFile = new ZipFile(new File("src/test/resources/TestFiles.zip"));
        ZipInputStream zipInputStream = new ZipInputStream(cl.getResourceAsStream("TestFiles.zip"));
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.getName().contains("csvExample.csv")) {
                try (InputStream inputStream = zipFile.getInputStream(entry)) {
                    CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                    List<String[]> content = reader.readAll();
                    String[] row = content.get(1);
                    assertThat(row[2]).isEqualTo("Greetings");
                }
            }
        }
    }

}
