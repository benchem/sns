package team.benchem.webapi.entity;

public class InvokeToken {
    private String instanceUrl;
    private String requestToken;

    public InvokeToken(String instanceUrl, String requestToken) {
        this.instanceUrl = instanceUrl;
        this.requestToken = requestToken;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public String getRequestToken() {
        return requestToken;
    }
}
