package dbshaker.core.testers;

import dbshaker.core.FrameworkRunner;
import dbshaker.core.Scores;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BrandsTester {

    public static String NAMES_FILE = "/dbshaker/brands.txt";

    public static int ID_MIN = 1;

    public static int ID_MAX = 10000;

    private final ArrayList<String> names;

    private final FrameworkRunner runner;

    public BrandsTester(FrameworkRunner runner) throws IOException {
        names = new ArrayList<>();
        try (final InputStream is = getClass().getResourceAsStream(NAMES_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                names.add(line.concat(" "));
            }
        }
        this.runner = runner;
    }

    public void clear() {
        this.names.clear();
    }

    public Scores insertData() {
        return Tester.test(ID_MIN, ID_MAX, id -> insertOne(id));
    }

    void insertOne(int id) throws Exception {
        runner.createBrand(id, names.get(RandomUtils.nextInt(0, names.size())).concat(RandomStringUtils.randomAscii(2)));
    }
}
