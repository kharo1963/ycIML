package com.example.bootIML.interpretator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

import com.example.bootIML.service.ArrayFilFiles;

public class Executer {

    public void Execute(ArrayList<Lex> poliz) {
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
                    if (StatD.TID.get(i).get_assign()) {
                        arguments.push(StatD.TID.get(i).get_value());
                        break;
                    } else
                        throw new RuntimeException("POLIZ: indefinite identifier");

                case LEX_NOT:
                    i = StatD.fromStack(arguments);
                    arguments.push(i == 0 ? 1 : 0);
                    break;

                case LEX_OR:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j > 0 || i > 0 ? 1 : 0);
                    break;

                case LEX_AND:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j > 0 && i > 0 ? 1 : 0);
                    break;

                case POLIZ_GO:
                    i = StatD.fromStack(arguments);
                    index = i - 1;
                    break;

                case POLIZ_FGO:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    if (j == 0) index = i - 1;
                    break;

                case LEX_WRITE:
                    j = StatD.fromStack(arguments);
                    System.out.println(j);
                    ArrayFilFiles.filFiles.add(j);
                    break;

                case LEX_READ:
                    int k;
                    i = StatD.fromStack(arguments);
                    if (StatD.TID.get(i).get_type() == TypeOfLex.LEX_INT) {
                        System.out.println("Input int value for " + StatD.TID.get(i).get_name());
                        Scanner in = new Scanner(System.in);
                        k = in.nextInt();
                        in.close();
                    } else {
                        String js;
                        while (true) {
                            System.out.println("Input boolean value (true or false) for " + StatD.TID.get(i).get_name());
                            Scanner in = new Scanner(System.in);
                            js = in.nextLine();
                            in.close();
                            if (!js.equals("true") && !js.equals("false")) {
                                System.out.println("Error in input:true/false");
                                continue;
                            }
                            k = (js.equals("true")) ? 1 : 0;
                            break;
                        }
                    }
                    StatD.TID.get(i).put_value(k);
                    StatD.TID.get(i).put_assign();
                    break;

                case LEX_GET:
                    int restArgVal;
                    String restArgStr;
                    i = StatD.fromStack(arguments);
                    restArgStr = StatD.restArg.get(i);
                    System.out.println("case LEX_GET restArgStr:");
                    System.out.println(restArgStr);
                    String[] readParams = restArgStr.split("/");
                    System.out.println("readParams:");
                    System.out.println(readParams[0]);
                    System.out.println(readParams[1]);
                    restArgVal = StatD.imlParamServiceImpl.readParam(readParams[0], readParams[1]);
                    System.out.println("restArgVal");
                    System.out.println(restArgVal);
                    i = StatD.fromStack(arguments);
                    StatD.TID.get(i).put_value(restArgVal);
                    StatD.TID.get(i).put_assign();
                    break;

                case LEX_SPINCUBE:
                    int[] spinCubeParams = new int[4];
                    spinCubeParams[3] = StatD.fromStack(arguments);
                    spinCubeParams[2] = StatD.fromStack(arguments);
                    spinCubeParams[1] = StatD.fromStack(arguments);
                    spinCubeParams[0] = StatD.fromStack(arguments);
                    System.out.println("LEX_SPINCUBE" + " " + spinCubeParams[0] + " " + spinCubeParams[1] + " " + spinCubeParams[2] + " " + spinCubeParams[3]);
                    ArrayFilFiles.filFiles.add("spinCube");
                    StatD.fileContent = StatD.graphicsService.createSpinCube(spinCubeParams[0], spinCubeParams[1], spinCubeParams[2], spinCubeParams[3]);
                    break;

                case LEX_PLUS:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(i + j);
                    break;

                case LEX_TIMES:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(i * j);
                    break;

                case LEX_MINUS:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j - i);
                    break;

                case LEX_SLASH:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    if (i != 0) {
                        arguments.push(j / i);
                        break;
                    } else
                        throw new RuntimeException("POLIZ:divide by zero");

                case LEX_EQ:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(i == j ? 1 : 0);
                    break;

                case LEX_LSS:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j < i ? 1 : 0);
                    break;

                case LEX_GTR:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j > i ? 1 : 0);
                    break;

                case LEX_LEQ:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j <= i ? 1 : 0);
                    break;

                case LEX_GEQ:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j >= i ? 1 : 0);
                    break;

                case LEX_NEQ:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    arguments.push(j != i ? 1 : 0);
                    break;

                case LEX_ASSIGN:
                    i = StatD.fromStack(arguments);
                    j = StatD.fromStack(arguments);
                    StatD.TID.get(j).put_value(i);
                    StatD.TID.get(j).put_assign();
                    arguments.push(i);
                    break;

                case LEX_SEMICOLON:
                    StatD.fromStack(arguments);
                    break;

                default:
                    throw new RuntimeException("POLIZ: unexpected elem");
            }//end of switch
            ++index;
        }//end of while
        System.out.println("Finish of executing!!!");
    }
}
