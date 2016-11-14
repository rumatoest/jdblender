package jdblender.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ReportWriter {

    public static void write(Path reportPath, Collection<Scores> scores, int factor) throws IOException {
        if (!Files.exists(reportPath)) {
            Files.createFile(reportPath);
            Files.write(reportPath, Arrays.asList(scoresHeader(scores)));
        }

        boolean noJit = "NONE".equals(System.getenv("JIT"));

        StringBuilder sb = new StringBuilder(
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .append(",").append(noJit ? "0" : "1")
            .append(",").append(factor);
        for (Scores s : scores) {
            sb.append(',');
//            sb.append(s.iterations()).append(',');
//            sb.append(s.timeSec()).append(',');
//            sb.append(BigDecimal.valueOf(s.timeSec()).setScale(2, RoundingMode.HALF_UP)).append(',');
            sb.append(BigDecimal.valueOf(s.avgMu()).setScale(6, RoundingMode.HALF_UP));
        }

        Files.write(reportPath, Collections.singletonList(sb), StandardOpenOption.APPEND);

        System.out.println("REPORT FILE: " + reportPath.toString());
    }

    static String scoresHeader(Collection<Scores> scores) {
        long code = 0L;
        StringBuilder sb = new StringBuilder("TIME,JIT,FACTOR");
        for (Scores s : scores) {
            code = code / 2 + s.getCode() / 2;
            sb.append(',');
//            sb.append(s.label()).append("[num runs],");
//            sb.append(s.label()).append("[time sec],");
            sb.append(s.label()).append(" [mu]");
        }
        return sb.toString();
    }
}
