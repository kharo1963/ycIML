package com.example.bootIML.interpretator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import com.example.bootIML.kafka.KafkaMessage;
import com.example.bootIML.service.ExecuterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Executer {

    private final ExecuterService executerService;

    public void Execute(SourceProgram sourceProgram) {
        ArrayList<Lex> poliz = sourceProgram.poliz;
        Lex currentPolizLex;
        Deque<Integer> arguments = new ArrayDeque<Integer>();
        int i, j;
        int index = 0;
        int polizSize = poliz.size();
        while (index < polizSize) {
            currentPolizLex = poliz.get(index);
            switch (currentPolizLex.getTypeOfLex()) {
                case LEX_TRUE:
                case LEX_FALSE:
                case LEX_NUM:
                case POLIZ_ADDRESS:
                case POLIZ_LABEL:
                    arguments.push(currentPolizLex.getValueOfLex());
                    break;

                case LEX_ID:
                    i = currentPolizLex.getValueOfLex();
                    if (sourceProgram.TID.get(i).get_assign()) {
                        arguments.push(sourceProgram.TID.get(i).get_value());
                        break;
                    } else
                        throw new RuntimeException("POLIZ: indefinite identifier");

                case LEX_NOT:
                    i = arguments.remove();
                    arguments.push(i == 0 ? 1 : 0);
                    break;

                case LEX_OR:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j > 0 || i > 0 ? 1 : 0);
                    break;

                case LEX_AND:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j > 0 && i > 0 ? 1 : 0);
                    break;

                case POLIZ_GO:
                    i = arguments.remove();
                    index = i - 1;
                    break;

                case POLIZ_FGO:
                    i = arguments.remove();
                    j = arguments.remove();
                    if (j == 0) index = i - 1;
                    break;

                case LEX_WRITE:
                    j = arguments.remove();
                    sourceProgram.resultList.add(j);
                    break;

                case LEX_GET:
                    int restArgVal;
                    String restArgStr;
                    i = arguments.remove();
                    restArgStr = sourceProgram.restArg.get(i);
                    log.debug("case LEX_GET restArgStr: " + restArgStr);
                    String[] readParams = restArgStr.split("/");
                    restArgVal = executerService.getImlParamServiceImpl().readParam(readParams[0], readParams[1]);
                    log.debug("restArgVal: " + restArgVal);
                    i = arguments.remove();
                    sourceProgram.TID.get(i).put_value(restArgVal);
                    sourceProgram.TID.get(i).put_assign();
                    break;

                case LEX_GET_KAFKA:
                    executerService.getConsumer().startMessage ();
                    break;

                case LEX_PUT_KAFKA:
                    executerService.getProducer().send(new KafkaMessage(1, "A simple test message"));
                    break;

                case LEX_SPINCUBE:
                    int[] spinCubeParams = new int[4];
                    spinCubeParams[3] = arguments.remove();
                    spinCubeParams[2] = arguments.remove();
                    spinCubeParams[1] = arguments.remove();
                    spinCubeParams[0] = arguments.remove();
                    log.debug("LEX_SPINCUBE" + " " + spinCubeParams[0] + " " + spinCubeParams[1] + " " + spinCubeParams[2] + " " + spinCubeParams[3]);
                    sourceProgram.resultList.add("spinCube");
                    sourceProgram.fileContent = executerService.getGraphicsService().createSpinCube(spinCubeParams[0], spinCubeParams[1], spinCubeParams[2], spinCubeParams[3]);
                    break;

                case LEX_PLUS:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(i + j);
                    break;

                case LEX_TIMES:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(i * j);
                    break;

                case LEX_MINUS:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j - i);
                    break;

                case LEX_SLASH:
                    i = arguments.remove();
                    j = arguments.remove();
                    if (i != 0) {
                        arguments.push(j / i);
                        break;
                    } else
                        throw new RuntimeException("POLIZ:divide by zero");

                case LEX_EQ:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(i == j ? 1 : 0);
                    break;

                case LEX_LSS:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j < i ? 1 : 0);
                    break;

                case LEX_GTR:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j > i ? 1 : 0);
                    break;

                case LEX_LEQ:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j <= i ? 1 : 0);
                    break;

                case LEX_GEQ:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j >= i ? 1 : 0);
                    break;

                case LEX_NEQ:
                    i = arguments.remove();
                    j = arguments.remove();
                    arguments.push(j != i ? 1 : 0);
                    break;

                case LEX_ASSIGN:
                    i = arguments.remove();
                    j = arguments.remove();
                    sourceProgram.TID.get(j).put_value(i);
                    sourceProgram.TID.get(j).put_assign();
                    arguments.push(i);
                    break;

                case LEX_SEMICOLON:
                    arguments.remove();
                    break;

                default:
                    throw new RuntimeException("POLIZ: unexpected elem");
            }//end of switch
            ++index;
        }//end of while
        log.info("Finish of executing!!!");
    }
}
