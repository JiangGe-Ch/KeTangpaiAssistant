package us.ch.jiangge.ketangpaiAssistant.entity;

//"id text primary key,"+
//        "contenttype text not null,"+
//        "title text,"+
//        "endtime text,"+
//        "mstatus text,"+
//        "warningcheckrate text,"+
//        "timestatus text)"

import java.text.SimpleDateFormat;
import java.util.Date;

import us.ch.jiangge.ketangpaiAssistant.R;
import us.ch.jiangge.ketangpaiAssistant.emuns.ContentTimeStatus;
import us.ch.jiangge.ketangpaiAssistant.emuns.ContentType;
import us.ch.jiangge.ketangpaiAssistant.emuns.ContentMstatus;

public class ContentItem {
    /**
     * 内容id
     */
    private String id;
    /**
     * 内容类型
     */
    private String contentType;
    /**
     * 标题
     */
    private String title;
    /**
     * 截止时间
     */
    private String endtime;
    /**
     * 完成状态
     */
    private String mstatus;
    /**
     * 查重率
     */
    private String warningchechrate;
    /**
     * 时间状态
     */
    private String timestatus;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程老师名字
     */
    private String teacher;

    /**
     * 课程班级
     */
    private String className;

    public ContentItem(String id, String contentType, String title, String endtime, String mstatus, String warningchechrate, String timestatus, String courseName, String teacher, String className) {
        this.id = id;
        this.contentType = contentType;
        this.title = title;
        this.endtime = endtime;
        this.mstatus = mstatus;
        this.warningchechrate = warningchechrate;
        this.timestatus = timestatus;
        this.courseName=courseName;
        this.teacher=teacher;
        this.className=className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
        return ContentType.nameOf(Integer.parseInt(this.contentType));
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndtime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(Long.parseLong(this.endtime)*1000);
        return sdf.format(date);
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getMstatus() {
        return ContentMstatus.nameByStatus(Integer.parseInt(this.mstatus));
    }

    public void setMstatus(String mstatus) {
        this.mstatus = mstatus;
    }

    public String getWarningchechrate() {
        return warningchechrate;
    }

    public void setWarningchechrate(String warningchechrate) {
        this.warningchechrate = warningchechrate;
    }

    public String getTimestatus() {
        return ContentTimeStatus.nameOfByStatus(Integer.parseInt(this.timestatus));
    }

    public void setTimestatus(String timestatus) {
        this.timestatus = timestatus;
    }

    public int getTypeImage(){
        switch (Integer.parseInt(this.contentType)){
            case 0:return R.mipmap.catalog;
            case 1:return R.mipmap.courseware;
            case 2:return R.mipmap.matarial;
            case 4:return R.mipmap.work;
            case 5:return R.mipmap.topic;
            case 6:return R.mipmap.test;
            case 7:return R.mipmap.notice;
            case 8:return R.mipmap.matarial;
        }
        return R.mipmap.unknown;
    }

    @Override
    public String toString() {
        return "ContentItem{" +
                "id='" + id + '\'' +
                ", contentType='" + contentType + '\'' +
                ", title='" + title + '\'' +
                ", endtime='" + endtime + '\'' +
                ", mstatus='" + mstatus + '\'' +
                ", warningchechrate='" + warningchechrate + '\'' +
                ", timestatus='" + timestatus + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacher='" + teacher + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
