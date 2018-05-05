package team.benchem.webapi.utils;

public enum InvokeStateCode implements StateCode {
    Auth_Denied(100001, "无权访问"),
    TargetService_NotFound(100002, "远程服务没注册"),
    TargetService_InstanceNotFound(1000003, "远程服务当前无可用实例"),
    SourceService_NotFound(100004, "请求服务没注册"),
    ;

    private final Integer stateCode;
    private final String message;

    InvokeStateCode(Integer stateCode, String message){
        this.stateCode = stateCode;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return stateCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
