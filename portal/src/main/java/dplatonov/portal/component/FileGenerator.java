package dplatonov.portal.component;

import dplatonov.portal.entity.ScannerConfigCutoff;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.entity.ScannerPreview;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FileGenerator {

  private final String tmpdir;

  public FileGenerator() throws IOException {
    tmpdir = Files.createTempDirectory("tmpDirPrefix").toFile()
        .getAbsolutePath();
  }

  public Resource getCsvFile(List<ScannerPreview> previews, ScannerConfigs scannerConfigs,
      ScannerConfigCutoff scannerConfigCutoff)
      throws IOException {
    String fileName =
        tmpdir + "/" + StringUtils.trimAllWhitespace(scannerConfigs.getName())
            .replaceAll("[^a-zA-Z]+", "") + ".csv";
    createCsvFile(previews, fileName, scannerConfigCutoff, scannerConfigs);
    Path filePath = Paths.get(fileName).toAbsolutePath().normalize();

    return new UrlResource(filePath.toUri());
  }

  private void createCsvFile(List<ScannerPreview> previews, String fileName,
      ScannerConfigCutoff scannerConfigCutoff, ScannerConfigs scannerConfigs) throws IOException {

    try (OutputStream outputStream = new FileOutputStream(fileName)) {
      outputStream.write(239);
      outputStream.write(187);
      outputStream.write(191);
      try (PrintWriter printWriter = new PrintWriter(
          new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
        printWriter.println("name, url, steps, start date, end date, value");
        StringBuilder sb = new StringBuilder();
        scannerConfigs.getScannerSteps().forEach(step -> {
          sb.append("<");
          sb.append(step.getTag());
          sb.append(">");
          sb.append(step.getAction());
          sb.append(";");
        });
        previews.forEach(preview -> {
          String sb2 = scannerConfigs.getName()
              + ","
              + scannerConfigs.getUrl()
              + ","
              + sb
              + ","
              + scannerConfigCutoff.getStartDate()
              + ","
              + scannerConfigCutoff.getStopDate()
              + ","
              + escapeSpecialCharacters(preview.getValue());
          printWriter.println(sb2);
        });
        printWriter.flush();
      }
    }
  }

  public String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
      data = data.replace("\"", "\"\"");
      escapedData = "\"" + data + "\"";
    }
    return escapedData;
  }
}
