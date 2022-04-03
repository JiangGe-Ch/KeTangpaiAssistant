package us.ch.jiangge.ketangpaiAssistant.entity;


//"id text primary key,"+
//        "contenttype text not null,"+
//        "title text,"+
//        "endtime text,"+
//        "mstatus text,"+
//        "warningcheckrate text,"+
//        "timestatus text)"

public class Content {
    /**
     * 内容id
     */
    private String id;
    /**
     * 内容类型
     */
    private String contenttype;
    /**
     * 内容标题
     */
    private String title;
    /**
     * 结束时间
     */
    private String endtime;
    /**
     * 提交状态   是 | 否
     */
    private String mstatus;
    /**
     * 查重率
     */
    private String warningcheckrate;
    /**
     * 时间状态    超时 | 未超时
     */
    private String timestatus;

    public Content(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getMstatus() {
        return mstatus;
    }

    public void setMstatus(String mstatus) {
        this.mstatus = mstatus;
    }

    public String getWarningcheckrate() {
        return warningcheckrate;
    }

    public void setWarningcheckrate(String warningcheckrate) {
        this.warningcheckrate = warningcheckrate;
    }

    public String getTimestatus() {
        return timestatus;
    }

    public void setTimestatus(String timestatus) {
        this.timestatus = timestatus;
    }
}
