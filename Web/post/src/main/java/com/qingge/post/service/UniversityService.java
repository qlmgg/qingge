package com.qingge.post.service;

import com.qingge.post.bean.api.university.SchoolInfoModel;
import com.qingge.post.bean.api.university.SchoolModel;
import com.qingge.post.bean.base.ResponseModel;
import com.qingge.post.bean.card.GroupCard;
import com.qingge.post.bean.card.PostCard;
import com.qingge.post.bean.card.UniversityCard;
import com.qingge.post.bean.db.Group;
import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.factory.GroupFactory;
import com.qingge.post.factory.UniversityFactory;
import com.qingge.post.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/university")
public class UniversityService extends BaseService {
    @GET
    @Path("/test")
    public String test() {
        return "测试成功";
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel createUniversity(SchoolModel model) {
        //检查
        if (!SchoolModel.check(model)) {
            return ResponseModel.buildParameterError();//返回参数异常
        }
        //拿到用户
        User owner = UserFactory.findById(model.getOwnerId());
        if (owner == null)
            return ResponseModel.buildNotFoundUserError("没找到这个用户");

        //查询是否已经存在此名字的学校
        University un = UniversityFactory.findByName(model.getName());
        if (un != null)
            return ResponseModel.buildNotFoundUserError("已经存在这个大学了");

        //创建学校
        University university = UniversityFactory.create(owner, model);
        if (university == null)
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_UNIVERSITY);
        return ResponseModel.buildOk();
    }

    /**
     * 根据范围查学校  比如xx省  xx市
     * */
//    @GET
//    @Path("/query/{range}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public ResponseModel<List<SchoolInfoModel>> queryUniversityInfo(@PathParam("range") String range) {
//
//        List<SchoolInfoModel> list = new ArrayList<>();
//
//        User user =getSelf();
//        //拿到自己大学的名字
//        String schoolName = user.getSchoolName();
//        //拿到自己的大学
//        University un = UniversityFactory.findByName(schoolName);
//
//        switch (range){
//            case "我的大学":
//                SchoolInfoModel model1 = new SchoolInfoModel(un.getName(),un.getId());
//                list.add(model1);
//                return ResponseModel.buildOk(list);
//            case "市内大学":
//                String city = un.getCity();
//                //根据城市查询大学
//                List<University> universityList1 = UniversityFactory.findByCity(city);
//
//                for (University university : universityList1) {
//                    //封装成SchoolInfoModel
//                    SchoolInfoModel model2 = new SchoolInfoModel(university.getName(),university.getId());
//                    list.add(model2);
//                }
//                return ResponseModel.buildOk(list);
//            case "省内大学":
//                //根据省份查询大学
//                String province = un.getProvince();
//                List<University> universityList2 = UniversityFactory.findByProvince(province);
//
//                for (University university : universityList2) {
//                    SchoolInfoModel model3 = new SchoolInfoModel(university.getName(),university.getId());
//                    list.add(model3);
//                }
//                return ResponseModel.buildOk(list);
//        }
//
//        return null;
//    }


    /**
     *
     * */
    @GET
    @Path("/query/{schoolId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<SchoolInfoModel> queryUniversityInfo(@PathParam("schoolId") String schoolId) {


        University university = UniversityFactory.findById(schoolId);
        if (university == null)
            return ResponseModel.buildNotFoundUniversityError("没找到学校");
        //找到的群集合
        List<Group> groups = GroupFactory.findGroupsBySchoolId(schoolId);
        //找到的帖子集合 第0页
        List<Post> posts = UniversityFactory.findByIdAndPage(schoolId,0);

        //转换
        List<PostCard> postCards = posts.stream()
                .map(PostCard::new)
                .collect(Collectors.toList());

        //转换
        List<GroupCard> groupCards = groups.stream()
                .map(GroupCard::new)
                .collect(Collectors.toList());



        return ResponseModel.buildOk(new SchoolInfoModel(4,postCards,groupCards));
    }


    /**
     *根据id拿到学校
     * */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UniversityCard> findUniversity(@PathParam("id") String id){
        if (id == null)
            return ResponseModel.buildParameterError();
        University university = UniversityFactory.findById(id);
        if (university == null)
            return ResponseModel.buildNotFoundUserError("没找到这个学校");

        return ResponseModel.buildOk(new UniversityCard(university));
    }
}
