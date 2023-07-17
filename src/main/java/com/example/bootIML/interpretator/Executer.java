package com.example.bootIML.interpretator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

import com.example.bootIML.service.ArrayFilFiles;

public class Executer {

    public void Execute(ArrayList<Lex> poliz) {
        Lex pc_el;
        Deque<Integer> args = new ArrayDeque<Integer>();
        int i, j, index = 0, size = poliz.size();
        while (index < size) {
            pc_el = poliz.get(index);
            switch (pc_el.get_type()) {
                case LEX_TRUE:
                case LEX_FALSE:
                case LEX_NUM:
                case POLIZ_ADDRESS:
                case POLIZ_LABEL:
                    args.push(pc_el.get_value());
                    break;

                case LEX_ID:
                    i = pc_el.get_value();
                    if (StatD.TID.get(i).get_assign()) {
                        args.push(StatD.TID.get(i).get_value());
                        break;
                    } else
                        throw new RuntimeException("POLIZ: indefinite identifier");

                case LEX_NOT:
                    i = StatD.from_st_i(args);
                    args.push(i == 0 ? 1 : 0);
                    break;

                case LEX_OR:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j > 0 || i > 0 ? 1 : 0);
                    break;

                case LEX_AND:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j > 0 && i > 0 ? 1 : 0);
                    break;

                case POLIZ_GO:
                    i = StatD.from_st_i(args);
                    index = i - 1;
                    break;

                case POLIZ_FGO:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    if (j == 0) index = i - 1;
                    break;

                case LEX_WRITE:
                    j = StatD.from_st_i(args);
                    System.out.println(j);
                    ArrayFilFiles.filFiles.add(j);
                    break;

                case LEX_READ:
                    int k;
                    i = StatD.from_st_i(args);
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
                    i = StatD.from_st_i(args);
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
                    i = StatD.from_st_i(args);
                    StatD.TID.get(i).put_value(restArgVal);
                    StatD.TID.get(i).put_assign();
                    break;

                case LEX_SPINCUBE:
                    int[] spinCubeParams = new int[4];
                    spinCubeParams[3] = StatD.from_st_i(args);
                    spinCubeParams[2] = StatD.from_st_i(args);
                    spinCubeParams[1] = StatD.from_st_i(args);
                    spinCubeParams[0] = StatD.from_st_i(args);
                    System.out.println("LEX_SPINCUBE" + " " + spinCubeParams[0] + " " + spinCubeParams[1] + " " + spinCubeParams[2] + " " + spinCubeParams[3]);
                    ArrayFilFiles.filFiles.add("spinCube");
                    StatD.graphicsService.createSpinCube(spinCubeParams[0], spinCubeParams[1], spinCubeParams[2], spinCubeParams[3]);
                    break;

                case LEX_PLUS:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(i + j);
                    break;

                case LEX_TIMES:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(i * j);
                    break;

                case LEX_MINUS:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j - i);
                    break;

                case LEX_SLASH:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    if (i != 0) {
                        args.push(j / i);
                        break;
                    } else
                        throw new RuntimeException("POLIZ:divide by zero");

                case LEX_EQ:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(i == j ? 1 : 0);
                    break;

                case LEX_LSS:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j < i ? 1 : 0);
                    break;

                case LEX_GTR:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j > i ? 1 : 0);
                    break;

                case LEX_LEQ:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j <= i ? 1 : 0);
                    break;

                case LEX_GEQ:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j >= i ? 1 : 0);
                    break;

                case LEX_NEQ:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    args.push(j != i ? 1 : 0);
                    break;

                case LEX_ASSIGN:
                    i = StatD.from_st_i(args);
                    j = StatD.from_st_i(args);
                    StatD.TID.get(j).put_value(i);
                    StatD.TID.get(j).put_assign();
                    args.push(i);
                    break;

                case LEX_SEMICOLON:
                    StatD.from_st_i(args);
                    break;

                default:
                    throw new RuntimeException("POLIZ: unexpected elem");
            }//end of switch
            ++index;
        }//end of while
        System.out.println("Finish of executing!!!");
    }
}
