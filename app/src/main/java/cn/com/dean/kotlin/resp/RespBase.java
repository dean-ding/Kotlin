package cn.com.dean.kotlin.resp;

import com.google.gson.JsonElement;

/**
 * Created: tvt on 17/10/10 16:01
 */
public class RespBase
{
    private RespBaseStatus status;
    private JsonElement result;
    private JsonElement vehicleid;

    public RespBaseStatus getStatus()
    {
        return status;
    }

    public JsonElement getResult()
    {
        return result;
    }

    public JsonElement getVehicleid()
    {
        return vehicleid;
    }
}
