import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;
import com.google.gson.JsonObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public class SpiderJson {

    public static void main(String[] args) throws IOException {

        String url="https://www.zhihu.com/api/v4/questions/36059311/answers?limit=1&offset=0";

        CloseableHttpClient closeableHttpClient=HttpClients.createDefault();

        //问题的编号偏移量
        int idOffset=0;
        ArrayList<Integer> idList=new ArrayList<>();

        while(true){

            HttpGet httpGet=new HttpGet(url);
            try{
                CloseableHttpResponse response=closeableHttpClient.execute(httpGet);
                String res=EntityUtils.toString(response.getEntity());

                JsonObject jObject=new JsonParser().parse(res).getAsJsonObject();
                JsonArray array=jObject.get("data").getAsJsonArray();
//                System.out.println(array);

                for (JsonElement jsonElement:array){
                    JsonObject jo=jsonElement.getAsJsonObject();
                    //获取每一个回答的id
                    int id=jo.get("id").getAsInt();
                    System.out.println(id);

                    //id存入数组
                    idList.add(id);
                }

                idOffset++;

                //偏移量超过回答数就break
                if (idOffset>104)
                    break;

                //改变url
                url="https://www.zhihu.com/api/v4/questions/36059311/answers?limit=1&offset="+idOffset;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//while结束

        String answerUrl;
        for (int i = 0; i < idList.size(); i++) {
            //取出id拼接
            answerUrl="https://www.zhihu.com/question/36059311/answer/"+idList.get(i);
            SpiderPic.getPic(answerUrl,idList.get(i));
        }

        System.out.println("结束了");
    }
}
