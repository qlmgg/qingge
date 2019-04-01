package com.qingge.post.bean.api.university;

import com.google.gson.annotations.Expose;

//学校范围切换返回的
public class SchoolInfoModel {
    @Expose
    private String schoolName;
    @Expose
    private String schoolId;

    public SchoolInfoModel(String schoolName, String schoolId) {
        this.schoolName = schoolName;
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
