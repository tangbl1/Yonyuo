

import okhttp3.*;

import java.io.*;

public class test1 {
    public static void main(String[] args) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=---011000010111000001101001");
        RequestBody body = RequestBody.create(mediaType, "-----011000010111000001101001--\r\n\r\n");
        Request request = new Request.Builder()
                .url("http://openapi.yonyoucloud.com/user/getMemberId?access_token=e5ef1f9310deaf52c799956caed93cdadf706898a7a89e5cb16df43c2a89567c&field=18340319070&type=1")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
    }
}
