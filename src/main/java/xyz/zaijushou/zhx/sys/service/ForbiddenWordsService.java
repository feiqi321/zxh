package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.AllForbiddenWords;
import xyz.zaijushou.zhx.sys.entity.WordsToSaveEntity;

/**
 * ForbiddenWordsService
 */
public interface ForbiddenWordsService {
	AllForbiddenWords query();

	void save(WordsToSaveEntity wordsToSaveEntity);

}