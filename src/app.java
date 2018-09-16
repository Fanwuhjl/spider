import java.io.File;
import java.io.IOException;

public class app {

    public static void main(String[] args) {

        String fileName="aaaaa";
        int answerId=2;
        File f = new File("./html/" + fileName);

        if (!f.exists()){
            f.mkdirs();
        }

        //html文件位置
        File file = new File("./html/" + fileName + "/" + answerId + ".html");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
