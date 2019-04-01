package com.qingge.yangsong.factory.model.db;

import com.qingge.yangsong.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;



/**
 * 我们APP中的基础的一个BaseDbModel，
 * 基础了数据库框架DbFlow中的基础类
 * 同时定义类我们需要的方法
 *
 * @version 1.0.0
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
