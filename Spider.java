import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Spider {
    public static void main(String[] args) throws IOException {

        //创建目标文件
        File toFile=new File("./html/ok3.txt");
        if (!toFile.exists()){
            try {
                toFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //要抓取的网页
//        String url="https://www.zhihu.com/question/34243513";
        String url="https://www.zhihu.com/api/v4/questions/36059311/answers";
        //开始抓取
        try {
            getHtml(url,toFile);
        } catch (Exception e) {
            System.out.println("抓取失败："+e.getMessage());
        }

//        //从文件解析
//        Document docHtml=Jsoup.parse(toFile,"UTF-8");
//        //获取需要的标签组
//        Elements eles=docHtml.select("a[href]");
//
//        //打印需要的内容
//        StringBuilder sb=new StringBuilder();
//        for(Element ele:eles){
//            //从每一个标签中获取属性
//            String link=ele.attr("href");
//            String title=ele.text();
//            System.out.println(title+": "+link);
//        }


    }

    /**
     * 抓取网页内容并写入文件
     * @param url
     * @param toFile
     * @throws Exception
     */
    public static void getHtml(String url,File toFile) throws Exception {

        //Joup
        Document doc=null;
        //使用Jsoup获取网页
        try {
            doc=Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new Exception("下载网页发生错误："+e.getMessage());
        }
        //将网页内容赋给字符串
        String contentHtml=doc.toString();
        //将获取的网页内容写入文件
        FileWriter fw=new FileWriter(toFile);
        BufferedWriter bw=new BufferedWriter(fw);
        //开始写入
        bw.write(contentHtml);
        //刷新，关闭流
        bw.flush();
        bw.close();
        fw.flush();
        fw.close();
        System.out.println("抓取成功");
    }
}
