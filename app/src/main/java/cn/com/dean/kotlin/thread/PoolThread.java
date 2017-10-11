package cn.com.dean.kotlin.thread;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import cn.com.dean.kotlin.resp.RespBase;

public class PoolThread
{
    private static PoolThread mPoolThread = null;
    private static ReentrantLock mLock = new ReentrantLock();

    private ExecutorService mExecutorService = null;

    private PoolThread()
    {
        mExecutorService = Executors.newFixedThreadPool(3);
    }

    public static PoolThread getInstance()
    {
        mLock.lock();
        if (mPoolThread == null)
        {
            mPoolThread = new PoolThread();
        }
        mLock.lock();
        return mPoolThread;
    }

    public void GetDelayed(final int id, final long time, final long timeout, final String url, final PoolThreadCallback callback)
    {
        mExecutorService.execute(new Runnable()
        {
            @Override
            public void run()
            {
                if (time > 0)
                {
                    try
                    {
                        Thread.sleep(time);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                int code = 0;
                // 创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient();
                mOkHttpClient.setConnectTimeout(timeout / 2, TimeUnit.SECONDS);
                mOkHttpClient.setWriteTimeout(timeout / 3, TimeUnit.SECONDS);
                mOkHttpClient.setReadTimeout(timeout / 3, TimeUnit.SECONDS);
                // 创建一个Request
                final Request request = new Request.Builder().url(url).build();
                RespBase respBean = null;
                Call call = mOkHttpClient.newCall(request);
                //请求加入调度
                call.enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Request request, IOException e)
                    {
                        System.out.println("onFailure " + e.toString());
                    }

                    @Override
                    public void onResponse(final Response response) throws IOException
                    {
                        System.out.println("onResponse " + response.isSuccessful());
                    }
                });
                //                    Response response = mOkHttpClient.newCall(request).execute();
                //                    if (response.isSuccessful())
                //                    {
                //                        String jsonStr = response.body().string();
                //                        System.out.println("htmlStr = " + jsonStr);
                //
                //                        Gson gson = new Gson();
                //                        respBean = gson.fromJson(jsonStr, RespBase.class);
                //                    }
                //                    else
                //                    {
                //                        throw new IOException("Unexpected code " + response);
                //                    }
                if (callback != null)
                {
                    callback.onPoolThreadRet(id, code, respBean);
                }
            }
        });
    }

}
