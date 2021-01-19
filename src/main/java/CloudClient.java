import java.io.*;

public class CloudClient {

    private static final String LOG = "transfer-file.txt";

    public void copy(String src, String dst) throws IOException {

        InputStream is = new FileInputStream(getClass().getResource(src).getPath());

        OutputStream os = new FileOutputStream(dst);

        int cur;

        while((cur = is.read()) != -1){
            os.write(cur);
        }
        os.close();
        is.close();


        //решение для больших файлов - через буфер
        /*
        InputStream is = new FileInputStream(getClass().getResource(src).getPath());

        OutputStream os = new FileOutputStream(dst);

        int read;
        byte [] buffer = new byte[1024];

        while((read = is.read(buffer)) != -1){
            os.write(buffer, 0, read);
        }
        os.close();
        is.close();
        */
    }

    public static void main(String[] args) throws IOException {
        new CloudClient().copy(LOG, LOG + "_copy");
    }
}
