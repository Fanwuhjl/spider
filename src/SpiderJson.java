import com.google.gson.*;
import com.google.gson.JsonObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SpiderJson {

    public static void main(String[] args) throws IOException {

        int questionId = 267536123;
        String questionTitle = "有哪些壁纸是你永远都不想换掉的";

//        String url="https://www.zhihu.com/api/v4/questions/36059311/answers?limit=1&offset=0";
//        String url="https://www.zhihu.com/api/v4/questions/40998854/answers?limit=1&offset=0";


        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        int idOffset = 1000;//问题的回答的编号的偏移量
//        int limit=1000;//一次取几个回答
        int total = 2000;//该问题有几个回答
        ArrayList<Integer> idList = new ArrayList<>();

        String url = "https://www.zhihu.com/api/v4/questions/" + questionId + "/answers?limit=1&offset=" + idOffset;

        while (true) {

            HttpGet httpGet = new HttpGet(url);

            String cookie = "_zap=bc5127a8-f6bd-4843-aee6-538d4ce3fe2e; d_c0=\"AEDgSDRTiA2PTnZBexCe2ZbZ5x8l1Bzqdf8=|1525266916\"; z_c0=\"2|1:0|10:1525864058|4:z_c0|92:Mi4xaHoyV0FnQUFBQUFBUU9CSU5GT0lEU1lBQUFCZ0FsVk5laVRnV3dDU1IwX09sSW9hVDBrT0pJRThqUFd1bXFNZVBR|901b7f8dd69aa0845058c43a299c71e1d047619975fcbaecdbe5a271a44d38ba\"; _xsrf=heQ6lrQBovG95p7ikuPUt4K667ee6mHT; q_c1=8b9adbf53f314bc3a6622c96e9846aa6|1536473512000|1524567624000; tgw_l7_route=200d77f3369d188920b797ddf09ec8d1; anc_cap_id=e86a11a6228949e09fef159c954a172a";

            httpGet.setHeader(new BasicHeader("Cookie",cookie));

            try {
                CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
                String res = EntityUtils.toString(response.getEntity());

                JsonObject jObject = new JsonParser().parse(res).getAsJsonObject();

//                System.out.println(jObject);

                JsonArray array = jObject.get("data").getAsJsonArray();
//                if (array==null){
//                    idOffset++;
//                    continue;
//                }
//                System.out.println(array);

                for (JsonElement jsonElement : array) {
                    JsonObject jo = jsonElement.getAsJsonObject();
                    //获取每一个回答的id
                    int id = jo.get("id").getAsInt();
                    System.out.println("这是第" + idOffset + "个id： " + id);

                    //id存入数组
                    idList.add(id);
                }

                idOffset++;

                //偏移量超过回答数就break
//                if (idOffset>104)
                if (idOffset > total)
                    break;

                //改变url
//                url="https://www.zhihu.com/api/v4/questions/36059311/answers?limit=1&offset="+idOffset;
                url = "https://www.zhihu.com/api/v4/questions/" + questionId + "/answers?limit=1&offset=" + idOffset;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//while结束

        //创建线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        String answerUrl;
//
        for (int i = 0; i < idList.size(); i++) {

            //取出id拼接
            answerUrl = "https://www.zhihu.com/question/" + questionId + "/answer/" + idList.get(i);
//            SpiderPic.getPicTogether(answerUrl,idList.get(i),questionTitle);
            System.out.println("第" + i + "回答处理完了。。。。。。。。。。。。。。。。。。。。。。。。");

//            System.out.println(idList.get(i));
//            System.out.println(questionTitle);

            //每次创建SpiderOic对象
            SpiderPic sp = new SpiderPic(answerUrl, idList.get(i), questionTitle);

            //交给线程池执行
            cachedThreadPool.execute(sp);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        cachedThreadPool.shutdown();

        try {
            boolean loop = true;
            do {    //等待所有任务完成
                loop = !cachedThreadPool.awaitTermination(5, TimeUnit.SECONDS);
            } while (loop);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("结束了");
    }
}
