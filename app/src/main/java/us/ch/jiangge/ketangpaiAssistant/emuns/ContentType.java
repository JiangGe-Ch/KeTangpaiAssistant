package us.ch.jiangge.ketangpaiAssistant.emuns;

public enum ContentType {
    CATALOG(0, "目录"),
    COURSEWARE(1, "互动课件"),
    MATERIAL(2, "资料"),
    WORK(4, "作业"),
    TOPIC(5, "话题"),
    TEST(6, "测试"),
    NOTICE(7, "公告"),
    MATERIAL2(8, "资料");

    private int typeCode;
    private String typeName;

    ContentType(int typeCode, String typeName) {
        this.typeCode=typeCode;
        this.typeName=typeName;
    }

    public int getTypeCode(){
        return typeCode;
    }

    public String getTypeName(){
        return typeName;
    }

    public void setTypeCode(int typeCode){
        this.typeCode=typeCode;
    }

    public static String nameOf(int typeCode){
        switch (typeCode){
            case 0:return "目录";
            case 1:return "互动课件";
            case 2:return "资料";
            case 4:return "作业";
            case 5:return "话题";
            case 6:return "测试";
            case 7:return "公告";
            case 8:return "资料";
        }
        return "未知code"+typeCode;
    }
}
