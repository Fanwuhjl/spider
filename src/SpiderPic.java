import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class SpiderPic implements Runnable {

    private String url;//目标网址
    private int answerId;//回答的id
    String fileName;

    public SpiderPic() {

    }

    public SpiderPic(String url, int answerId, String fileName) {
        this.url = url;
        this.answerId = answerId;
        this.fileName = fileName;
    }

//    public static void main(String[] args) throws IOException {
//
//    }

    /**
     * 下载一个页面的所有图片
     *
     * @param url
     * @param answerId
     * @throws IOException
     */
    public static void getPic(String url, int answerId, String fileName) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        String content = EntityUtils.toString(entity, "UTF-8");

        File f = new File("./html/" + fileName);
        f.mkdirs();
        //html文件位置
        File file = new File("./html/" + fileName + "/" + answerId + ".html");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);//重写会覆盖
        bw.write(content);
        System.out.println("写入文件完成");

        //解析
        Document doc = Jsoup.parse(content);
        //获取需要的标签
        Elements eles = doc.select("img[data-actualsrc]");

        int index = 0;
        for (Element ele : eles) {
            String link = ele.attr("data-actualsrc");

            HttpGet getPic = new HttpGet(link);
            System.out.println(link);
            CloseableHttpResponse picResponse = httpClient.execute(getPic);
            HttpEntity picEntity = picResponse.getEntity();

            InputStream inputStream = picEntity.getContent();

            // 获取图片扩展名
            String exName = link.substring(link.lastIndexOf('.') + 1, link.length());

            //存入指定文件
//            FileUtils.copyToFile(inputStream, new File("./gakkiPic/" + answerId + "/" + (index++) + ".jpg"));
//            FileUtils.copyToFile(inputStream, new File("./石原里美Pic/" + answerId + "/" + (index++) + ".jpg"));
            FileUtils.copyToFile(inputStream, new File("./" + fileName + "Pic/" + answerId + "/" + (index++) + exName));
            picResponse.close();
        }
        response.close();
        httpClient.close();
    }

    /**
     * 所有图片放在一起
     *
     * @param url
     * @param answerId
     * @param fileName
     * @throws IOException
     */
    public static void getPicTogether(String url, int answerId, String fileName) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);

        String cookie = "_zap=bc5127a8-f6bd-4843-aee6-538d4ce3fe2e; d_c0=\"AEDgSDRTiA2PTnZBexCe2ZbZ5x8l1Bzqdf8=|1525266916\"; z_c0=\"2|1:0|10:1525864058|4:z_c0|92:Mi4xaHoyV0FnQUFBQUFBUU9CSU5GT0lEU1lBQUFCZ0FsVk5laVRnV3dDU1IwX09sSW9hVDBrT0pJRThqUFd1bXFNZVBR|901b7f8dd69aa0845058c43a299c71e1d047619975fcbaecdbe5a271a44d38ba\"; _xsrf=heQ6lrQBovG95p7ikuPUt4K667ee6mHT; q_c1=8b9adbf53f314bc3a6622c96e9846aa6|1536473512000|1524567624000; tgw_l7_route=200d77f3369d188920b797ddf09ec8d1; anc_cap_id=e86a11a6228949e09fef159c954a172a";

        httpGet.setHeader(new BasicHeader("Cookie", cookie));

        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        String content = EntityUtils.toString(entity, "UTF-8");

        System.out.println(fileName);
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

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);//重写会覆盖
        bw.write(content);
        System.out.println("写入文件完成");

        //解析
        Document doc = Jsoup.parse(content);
        //获取需要的标签
        Elements eles = doc.select("img[data-actualsrc]");

        int index = 0;
        for (Element ele : eles) {
            String link = ele.attr("data-actualsrc");

            HttpGet getPic = new HttpGet(link);
            System.out.println(link);
            CloseableHttpResponse picResponse = httpClient.execute(getPic);
            HttpEntity picEntity = picResponse.getEntity();

            InputStream inputStream = picEntity.getContent();

            // 获取图片扩展名
            String exName = link.substring(link.lastIndexOf('.') + 1, link.length());

            //存入指定文件
//            FileUtils.copyToFile(inputStream, new File("./gakkiPic/" + answerId + "/" + (index++) + ".jpg"));
            FileUtils.copyToFile(inputStream, new File("./" + fileName + "Pics/" + answerId + "_" + (index++) + "." + exName));

            System.out.println("图" + index);
//            FileUtils.copyToFile(inputStream, new File("./" + fileName + "Pics"));
            picResponse.close();
            inputStream.close();
        }

        response.close();
        httpClient.close();
    }

    @Override
    public void run() {
        try {
//            getPic(url, answerId,fileName);
            getPicTogether(url, answerId, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
