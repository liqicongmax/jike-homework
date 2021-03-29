package homework.work02;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientUtils {

    /**
     * POST             请求
     * @param url       请求地址
     * @param params    请求参数
     * @param encode    编码格式
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public JSONObject doPostMethod(String url, Map<String,String> params, String encode) throws ClientProtocolException, IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                form.add(new BasicNameValuePair(name,params.get(name)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form,encode);
            httpPost.setEntity(entity);
        }
        HttpResponse response = httpclient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        JSONObject obj = JSONObject.parseObject(result);
        return obj;
    }

    /**
     * GET
     * @param url
     * @param params
     * @param encode
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public JSONObject doGetMethod(String url,List<NameValuePair> params,String encode) throws ClientProtocolException, IOException{
        HttpClient httpclient = HttpClients.createDefault();
        //参数转换为字符串
        String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params, encode));
        url = url + "?" + paramsStr;
        // 创建httpget.
        HttpGet httpget = new HttpGet(url);
        System.out.println("executing request " + httpget.getURI());
        HttpResponse response = httpclient.execute(httpget);
        String result = EntityUtils.toString(response.getEntity());
        JSONObject obj = JSONObject.parseObject(result);
        return obj;
    }

    public static void main(String[] args)throws Exception {
        ClientUtils clientUtils=new ClientUtils();
        clientUtils.doGetMethod("localhost:8801",new ArrayList<>(),"UTF-8");
    }
}