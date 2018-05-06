package team.benchem.webapi.advisor;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public class RequestContext {
    static final HashMap<Long, RequestContext> globalContexts = new HashMap<>();

    static void appendRequestContext(Long threadId, RequestContext context){
        globalContexts.put(threadId, context);
    }

    static void removeRequestContext(Long threadId){
        if(globalContexts.containsKey(threadId)){
            globalContexts.remove(threadId);
        }
    }

    protected static RequestContext createRequestContext(){
        Thread currThread = Thread.currentThread();
        Long threadId = currThread.getId();

        RequestContext context = new RequestContext();
        context.properties.put("threadId", threadId);
        appendRequestContext(threadId, context);
        return context;
    }

    protected static void removeCurrentRequestContext(){
        Thread currThread = Thread.currentThread();
        Long threadId = currThread.getId();
        removeRequestContext(threadId);
    }

    public static RequestContext getCurrentRequestContext(){
        Thread currThread = Thread.currentThread();
        Long threadId = currThread.getId();
        if(globalContexts.containsKey(threadId)){
            return globalContexts.get(threadId);
        }
        return null;
    }

    public JSONObject properties = new JSONObject();
}
