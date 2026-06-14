package com.zxw.aichat.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.aichat.base.entity.AiModel;
import com.zxw.aichat.base.mapper.AiModelMapper;
import com.zxw.aichat.base.service.AiModelService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AiModelServiceImpl extends ServiceImpl<AiModelMapper, AiModel> implements AiModelService {

    @Override
    public List<AiModel> listModels(Integer enabled) {
        LambdaQueryWrapper<AiModel> wrapper = new LambdaQueryWrapper<AiModel>()
                .eq(enabled != null, AiModel::getEnabled, enabled)
                .orderByAsc(AiModel::getSort)
                .orderByAsc(AiModel::getId);
        return list(wrapper);
    }

    @Override
    public Long createModel(AiModel model) {
        save(model);
        return model.getId();
    }

    @Override
    public boolean updateModel(Long id, AiModel model) {
        model.setId(id);
        return updateById(model);
    }

    @Override
    public boolean deleteModel(Long id) {
        return removeById(id);
    }
}
