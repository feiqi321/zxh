package xyz.zaijushou.zhx.sys.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.AllForbiddenWords;
import xyz.zaijushou.zhx.sys.entity.WordsToSaveEntity;
import xyz.zaijushou.zhx.sys.service.ForbiddenWordsService;

/**
 * ForbiddenWordsController
 */
@RestController
@RequestMapping("/forbiddenwords")
public class ForbiddenWordsController {
    @Resource
    private ForbiddenWordsService forbiddenWordsService;

    @PostMapping("/query")
    public WebResponse query() {
        AllForbiddenWords words = forbiddenWordsService.query();
        return WebResponse.success(words);
    }

    @PostMapping("/save")
    public WebResponse save(@RequestBody WordsToSaveEntity wordsToSaveEntity) {
        forbiddenWordsService.save(wordsToSaveEntity);
        return WebResponse.success();
    }
}