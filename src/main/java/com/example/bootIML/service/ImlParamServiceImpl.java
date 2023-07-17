package com.example.bootIML.service;

import com.example.bootIML.model.ImlParam;
import com.example.bootIML.repository.ImlParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.example.bootIML.interpretator.StatD;

import java.util.List;
import java.util.Optional;

@Service
public class ImlParamServiceImpl implements ImlParamService {

    @Autowired
    private ImlParamRepository imlParamRepository;

    ImlParamServiceImpl () {StatD.imlParamServiceImpl = this;}

    @Override
    public void create(ImlParam imlParam) {
        imlParamRepository.save(imlParam);
    }

    @Override
    public List<ImlParam>  readAll() {
        return imlParamRepository.findAll();
    }

    @Override
    public ImlParam read(int id) {
        System.out.println("imlParamRepository.read");
        return imlParamRepository.getOne(id);
    }

    @Override
    public boolean update(ImlParam imlParam, int id) {
        if (imlParamRepository.existsById(id)) {
            imlParam.setId(id);
            imlParamRepository.save(imlParam);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        if (imlParamRepository.existsById(id)) {
            imlParamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Integer readParam(String progName, String paramName) {
        ImlParam imlParam = new ImlParam();
        imlParam.setProgName(progName);
        imlParam.setParamName(paramName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("paramVal")
                .withIgnorePaths("id")
                .withIncludeNullValues();
        Example<ImlParam> example = Example.of(imlParam, matcher);
        Optional<ImlParam> optImlParam = imlParamRepository.findOne(example);
        if (optImlParam.isPresent()) {
            ImlParam repoImlParam = optImlParam.get();
            return repoImlParam.getParamVal();
        }
        return 0;
    }
}
