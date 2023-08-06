package com.example.bootIML.service;

import com.example.bootIML.kafka.Consumer;
import com.example.bootIML.kafka.Producer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class ExecuterService {
    private final ImlParamServiceImpl imlParamServiceImpl;
    private final GraphicsService graphicsService;
    private final Producer producer;
    private final Consumer consumer ;
}
