package com.zxw.aichat.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.aichat.base.entity.AiModel;
import java.util.List;

public interface AiModelService extends IService<AiModel> {

    List<AiModel> listModels(Integer enabled);

    Long createModel(AiModel model);

    boolean updateModel(Long id, AiModel model);

    boolean deleteModel(Long id);
}
