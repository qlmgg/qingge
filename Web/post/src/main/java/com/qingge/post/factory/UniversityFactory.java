package com.qingge.post.factory;

import com.qingge.post.bean.api.university.UniversityModel;
import com.qingge.post.bean.db.Comment;
import com.qingge.post.bean.db.Post;
import com.qingge.post.bean.db.University;
import com.qingge.post.bean.db.User;
import com.qingge.post.utils.Hib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UniversityFactory {
    //通过id找到大学
    public static University findById(String id) {
        return Hib.query(session -> session.get(University.class, id));
    }

    // 通过Name找到University
    public static University findByName(String name) {
        return Hib.query(session -> (University) session
                .createQuery("from University where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    // 通过城市找到University 集合
    @SuppressWarnings("unchecked")
    public static List<University> findByCity(String city) {

        return Hib.query(session -> (List<University>) session.createQuery("from University where city=:city")
                .setParameter("city",city)
                .list());
    }


    // 通过省份找到University 集合
    @SuppressWarnings("unchecked")
    public static List<University> findByProvince(String province) {

        return Hib.query(session -> (List<University>) session.createQuery("from University where province=:province")
                .setParameter("province",province)
                .list());
    }



    //根据查询大学id查帖子
    @SuppressWarnings("unchecked")
    public static List<Post> postsById(String universityId) {
        return Hib.query(session -> (List<Post>)session.createQuery("from Post where UniversityId=:universityId order by updateAt desc ")
        .setParameter("universityId",universityId).setMaxResults(5)
        .list());
    }



//    根据查询大学id查帖子分页查询
    @SuppressWarnings("unchecked")
    public static List<Post> findByIdPage(String universityId,int page) {
        return Hib.query(session -> (List<Post>)session.createQuery("from Post where UniversityId=:universityId order by updateAt desc")
                .setParameter("universityId",universityId)
                .setFirstResult(page*8)
                .setMaxResults(8)
                .list());
    }



    //创建大学
    public static University create(User owner, UniversityModel model) {

        University university = new University(model, owner);
        return Hib.query(session -> {
            session.save(university);
            return university;
        });
    }

}
