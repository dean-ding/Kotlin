package cn.com.dean.kotlin.thread;

import cn.com.dean.kotlin.resp.RespBase;

public interface PoolThreadCallback
{
    public void onPoolThreadRet(int id, int code, RespBase respBase);
}
