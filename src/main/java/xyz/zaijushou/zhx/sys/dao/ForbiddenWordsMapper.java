package xyz.zaijushou.zhx.sys.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;

import xyz.zaijushou.zhx.sys.entity.ForbiddenWord;

/**
 * ForbiddenWordsMapper
 */
public interface ForbiddenWordsMapper {

	List<ForbiddenWord> queryAllWords();

	void deleteWordsWithType(Integer wordtype);

	void saveWordsWithType(@Param("list") List<ForbiddenWord> list, Integer wordtype);

}