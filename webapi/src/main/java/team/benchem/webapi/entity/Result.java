package team.benchem.webapi.entity;

public class Result {
    Integer statecode;
    String errmsg;
    Object result;

    public Result(){
        statecode = 0;
        errmsg = "";
        result = null;
    }

    public Result(Integer statecode, String errmsg) {
        this.statecode = statecode;
        this.errmsg = errmsg;
    }

    public Result(Object result) {
        this.statecode = 0;

        this.errmsg = "";
        this.result = result;
    }

    public Integer getStatecode() {
        return statecode;
    }

    public void setStatecode(Integer statecode) {
        this.statecode = statecode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
