package us.ch.jiangge.ketangpaiAssistant.emuns;

public enum ContentTimeStatus {
    NORMAL(2, "未过期"),
    PAST(3, "已过期");

    private int status;
    private String typeName;

    ContentTimeStatus(int status, String typeName) {
        this.status =status;
        this.typeName=typeName;
    }

    public static String nameOfByStatus(int status){
        switch (status){
            case 2:return "未过期";
            case 3:return "已过期";
        }
        return "未知code"+status;
    }
}
