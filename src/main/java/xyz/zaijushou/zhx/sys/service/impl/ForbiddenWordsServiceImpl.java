package xyz.zaijushou.zhx.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.zaijushou.zhx.sys.dao.ForbiddenWordsMapper;
import xyz.zaijushou.zhx.sys.entity.AllForbiddenWords;
import xyz.zaijushou.zhx.sys.entity.ForbiddenWord;
import xyz.zaijushou.zhx.sys.entity.WordsToSaveEntity;
import xyz.zaijushou.zhx.sys.service.ForbiddenWordsService;
import xyz.zaijushou.zhx.utils.RedisUtils;

/**
 * ForbiddenWordsServiceImpl
 */
@Service
public class ForbiddenWordsServiceImpl implements ForbiddenWordsService {
    @Resource
    private ForbiddenWordsMapper forbiddenWordsMapper;

    @Override
    public AllForbiddenWords query() {
        List<ForbiddenWord> list = forbiddenWordsMapper.queryAllWords();
        List<ForbiddenWord> dataCollectionType = new ArrayList<ForbiddenWord>();
        List<ForbiddenWord> remarkType = new ArrayList<ForbiddenWord>();
        for (ForbiddenWord forbiddenWord : list) {
            if (forbiddenWord.getWordtype() == 0) {
                dataCollectionType.add(forbiddenWord);
            } else if (forbiddenWord.getWordtype() == 1) {
                remarkType.add(forbiddenWord);
            }
        }
        AllForbiddenWords allForbiddenWords = new AllForbiddenWords();
        allForbiddenWords.setDataCollectionType(dataCollectionType);
        allForbiddenWords.setRemarkType(remarkType);
        return allForbiddenWords;
    }

    @Override
    @Transactional
    public void save(WordsToSaveEntity wordsToSaveEntity) {
        Integer wordtype = wordsToSaveEntity.getWordtype();
        List<ForbiddenWord> list = wordsToSaveEntity.getWords();
        for (int i = 0; i < list.size(); i++) {
            ForbiddenWord word = list.get(i);
            word.setWord(word.getWord().toLowerCase());
            list.set(i, word);
        }
        forbiddenWordsMapper.deleteWordsWithType(wordtype);
        forbiddenWordsMapper.saveWordsWithType(list,wordtype);

        if(wordtype == 0){
            RedisUtils.refreshForbiddenWordsDataCollection(list);
        }else{
            RedisUtils.refreshForbiddenWordsRemark(list);
        }
    }
}