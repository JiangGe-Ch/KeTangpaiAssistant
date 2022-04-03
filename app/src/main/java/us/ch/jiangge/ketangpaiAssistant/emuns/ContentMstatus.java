package us.ch.jiangge.ketangpaiAssistant.emuns;

public enum ContentMstatus {
    FINISHED(1, "已完成"),
    RESPONDED(4, "已批改"),
    UNFINISHED(0, "未完成");

    private int status;
    private String name;

    ContentMstatus(int status, String name) {
        this.status=status;
        this.name=name;
    }

    public static String nameByStatus(int mstatus){
        switch (mstatus){
            case 0:return "未完成";
            case 1:return "已完成";
            case 4:return "已批改";
        }
        return "未知mstatus"+mstatus;
    }
}
