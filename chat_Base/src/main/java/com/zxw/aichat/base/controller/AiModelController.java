package com.zxw.aichat.base.controller;

import com.zxw.aichat.base.annotation.ORpose;
import com.zxw.aichat.base.entity.AiModel;
import com.zxw.aichat.base.service.AiModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ORpose
@Api(tags = "AI模型接口")
@RestController
@RequestMapping("/base/models")
public class AiModelController {

    private final AiModelService aiModelService;

    public AiModelController(AiModelService aiModelService) {
        this.aiModelService = aiModelService;
    }

    @ApiOperation("查询模型列表")
    @GetMapping
    public List<AiModel> list(
            @ApiParam(value = "是否启用：1启用，0禁用")
            @RequestParam(required = false) Integer enabled) {
        return aiModelService.listModels(enabled);
    }

    @ApiOperation("查询模型详情")
    @GetMapping("/{id}")
    public AiModel getById(
            @ApiParam(value = "模型配置ID", required = true)
            @PathVariable Long id) {
        return aiModelService.getById(id);
    }

    @ApiOperation("新增模型")
    @PostMapping
    public Long create(
            @ApiParam(value = "模型配置信息", required = true)
            @RequestBody AiModel model) {
        return aiModelService.createModel(model);
    }

    @ApiOperation("更新模型")
    @PutMapping("/{id}")
    public Boolean update(
            @ApiParam(value = "模型配置ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "模型配置信息", required = true)
            @RequestBody AiModel model) {
        return aiModelService.updateModel(id, model);
    }

    @ApiOperation("删除模型")
    @DeleteMapping("/{id}")
    public Boolean delete(
            @ApiParam(value = "模型配置ID", required = true)
            @PathVariable Long id) {
        return aiModelService.deleteModel(id);
    }
}
