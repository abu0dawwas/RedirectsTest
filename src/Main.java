
import java.io.*;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
/**
 * Created by mdawwas on 3/5/18.
 */
public class Main {

    private static final String REDIRECTS_TXT = "redirects.txt";
    private static final String FAILED_TXT = "failed.txt";
    private static final String SUCCESS_TXT = "success.txt";
    private static final String SITE_LINK= "http://dev.nejm.org";


    public static void main(String[] args) throws IOException {
        BufferedWriter failedWriter = new BufferedWriter(new FileWriter(FAILED_TXT));
        BufferedWriter successWriter = new BufferedWriter(new FileWriter(SUCCESS_TXT));
        FileReader fr = new FileReader(REDIRECTS_TXT);
        BufferedReader br = new BufferedReader(fr);
        String sCurrentLine;
        int i = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            ++i;
            if(sCurrentLine.trim().length() == 0)
                continue;
            int spaceIndex = sCurrentLine.indexOf(' ');
            String path = sCurrentLine.subSequence(0,spaceIndex).toString().trim();
            String expectedUrl = SITE_LINK + sCurrentLine.substring(spaceIndex,sCurrentLine.length()).trim();
            String url = SITE_LINK + path;
            String newUrl = null;
            try{
                Response response = Jsoup.connect(url).execute();
                newUrl = response.url().toURI().toString();
                if(!newUrl.equalsIgnoreCase(expectedUrl))
                    throw new Exception("didnt work, path : " + path + ", expected : " + expectedUrl + ", actual : " + newUrl);
                successWriter.write("Success for path: " + path + ", line number: " + i);
                successWriter.newLine();
                successWriter.flush();
            }catch (Exception e){
                failedWriter.write(sCurrentLine);
                failedWriter.newLine();
                failedWriter.flush();
            }
        }
        failedWriter.close();
        successWriter.close();
        br.close();



    }
}
