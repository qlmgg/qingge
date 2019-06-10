package com.qingge.yangsong.factory.data.helper;

import com.qingge.yangsong.factory.Factory;
import com.qingge.yangsong.factory.model.RspModel;

import com.qingge.yangsong.factory.model.card.GroupCard;
import com.qingge.yangsong.factory.model.card.LoadPostCard;
import com.qingge.yangsong.factory.model.card.UniversityCard;
import com.qingge.yangsong.factory.model.db.Group;
import com.qingge.yangsong.factory.model.db.University;
import com.qingge.yangsong.factory.model.db.University_Table;
import com.qingge.yangsong.factory.model.db.User;
import com.qingge.yangsong.factory.net.Network;
import com.qingge.yangsong.factory.net.RemoteService;
import com.qingge.yangsong.factory.presenter.community.CommunityContract;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolHelper {

    //根据学校id查询学校  远程
    private static University findUniversityFormNet(String id) {
        RemoteService service = Network.remote();
        try {
            Response<RspModel<UniversityCard>> response = service.findUniversity(id).execute();
            UniversityCard card = response.body().getResult();
            //保存的操作
            Factory.getSchoolCenter().dispatch(card);

            return card.build();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    //根据学校id查询学校  从本地
    private static University findUniversityFormLocal(String id) {
        return SQLite.select()
                .from(University.class)
                .where(University_Table.id.eq(id))
                .querySingle();

    }

    //根据学校id查询学校   先本地,没有的话在远程
    public static University findUniversity(final String id) {
        final University[] university = {findUniversityFormLocal(id)};
        if (university[0] == null) { //本地无  走远程
            Factory.runOnAsync(() -> {
                University university1 = findUniversityFormNet(id);
                if (university1 != null)
                    university[0] = university1;
                else
                    university[0] = null;
            });
            return university[0];
        }
        return university[0];
    }

}
