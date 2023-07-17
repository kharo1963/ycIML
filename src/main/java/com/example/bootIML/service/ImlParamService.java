package com.example.bootIML.service;

import com.example.bootIML.model.ImlParam;

import java.util.List;
public interface ImlParamService {
    /**
     * Создает новый параметр
     * @param imlParam - параметр для создания
     */
    void create(ImlParam imlParam);

    /**
     * Возвращает список всех имеющихся параметров
     * @return список параметров
     */
    List<ImlParam> readAll();

    /**
     * Возвращает параметр по его ID
     * @param id - ID параметра
     * @return - объект параметр с заданным ID
     */
    ImlParam read(int id);

    /**
     * Обновляет параметр с заданным ID,
     * в соответствии с переданным параметром
     * @param imlParam - параметр в соответсвии с которым нужно обновить данные
     * @param id - id параметра которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(ImlParam imlParam, int id);

    /**
     * Удаляет параметр с заданным ID
     * @param id - id параметра, которого нужно удалить
     * @return - true если параметр был удален, иначе false
     */
    boolean delete(int id);
}
