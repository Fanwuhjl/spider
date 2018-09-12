import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;

public class SpiderPic {

    public static void main(String[] args) throws IOException {

    }

    /**
     * 下载一个页面的所有图片
     * @param url
     * @param answerId
     * @throws IOException
     */
    public static void getPic(String url,int answerId) throws IOException {
        CloseableHttpClient httpClient=HttpClients.createDefault();

        HttpGet httpGet=new HttpGet(url);

        CloseableHttpResponse response=httpClient.execute(httpGet);

        HttpEntity entity=response.getEntity();

        String content=EntityUtils.toString(entity,"UTF-8");

        FileWriter fw=new FileWriter("./html/gakki.html");
        BufferedWriter bw=new BufferedWriter(fw);//重写会覆盖
        bw.write(content);
        System.out.println("写入文件完成");

        //解析
        Document doc=Jsoup.parse(content);
        //获取需要的标签
        Elements eles=doc.select("img[data-actualsrc]");

        int index=0;
        for(Element ele:eles){
            String link=ele.attr("data-actualsrc");

            HttpGet getPic=new HttpGet(link);
            System.out.println(link);
            CloseableHttpResponse picResponse=httpClient.execute(getPic);
            HttpEntity picEntity=picResponse.getEntity();

            InputStream inputStream=picEntity.getContent();

            //存入指定文件
            FileUtils.copyToFile(inputStream,new File("./gakkiPic/"+answerId+"/"+(index++)+".jpg"));
            picResponse.close();
        }
        response.close();
        httpClient.close();
    }
}
