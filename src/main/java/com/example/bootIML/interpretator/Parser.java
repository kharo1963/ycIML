package com.example.bootIML.interpretator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Parser {
    int currentLexVal;
    Lex currentLex;
    TypeOfLex currentLexType;
    Scanner scan;
    Deque<Integer> stackInteger = new ArrayDeque<>();
    Deque<TypeOfLex> stackTypeOfLex = new ArrayDeque<>();
    public ArrayList<Lex> poliz = new ArrayList<>();

    public Parser(String program) {
        scan = new Scanner(program);
    }

    public void analyze() {
        getNextLex();
        parseProgram();
        checkTypeOfLex(currentLex, TypeOfLex.LEX_FIN);

        System.out.println("poliz");
        for (Lex l : poliz) {
            System.out.println(l.toString());
            System.out.println(l.typeOfLex);
            System.out.println(l.valueOfLex);
        }
        scan.freeResourse();

        System.out.println();
        System.out.println("Yes!!!");
    }

    private void parseProgram() {
        checkTypeOfLex(currentLex, TypeOfLex.LEX_PROGRAM);
        getNextLex();
        parseDefinition();
        checkTypeOfLex(currentLex, TypeOfLex.LEX_SEMICOLON);
        getNextLex();
        parseProgramBody();
    }

    private void parseDefinition() {
        checkTypeOfLex(currentLex, TypeOfLex.LEX_VAR);
        getNextLex();
        parseDefinitionOneType();
        while (currentLexType == TypeOfLex.LEX_COMMA) {
            getNextLex();
            parseDefinitionOneType();
        }
    }

    private void parseDefinitionOneType() {
        checkTypeOfLex(currentLex, TypeOfLex.LEX_ID);
        stackInteger.push(currentLexVal);
        getNextLex();
        while (currentLexType == TypeOfLex.LEX_COMMA) {
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_ID);
            stackInteger.push(currentLexVal);
            getNextLex();
        }
        checkTypeOfLex(currentLex, TypeOfLex.LEX_COLON);
        getNextLex();
        if (currentLexType == TypeOfLex.LEX_INT) {
            updateTID(TypeOfLex.LEX_INT);
        } else if (currentLexType == TypeOfLex.LEX_BOOL) {
            updateTID(TypeOfLex.LEX_BOOL);
        } else {
            throw new RuntimeException(currentLex.toString());
        }
        getNextLex();
    }

    private void parseProgramBody() {
        checkTypeOfLex(currentLex, TypeOfLex.LEX_BEGIN);
        getNextLex();
        parseSentences();
        while (currentLexType == TypeOfLex.LEX_SEMICOLON) {
            getNextLex();
            parseSentences();
        }
        if (currentLexType == TypeOfLex.LEX_END) {
            getNextLex();
        } else {
            throw new RuntimeException(currentLex.toString());
        }
    }

    private void parseSentences() {
        int pl0, pl1, pl2, pl3;
        int idCnt = 0;
        int previousLexVal;
        TypeOfLex previousLexType;

        if (currentLexType == TypeOfLex.LEX_IF) {
            getNextLex();
            parseExpression();
            eqBool();
            pl2 = poliz.size();
            poliz.add(new Lex());
            poliz.add(new Lex(TypeOfLex.POLIZ_FGO));
            checkTypeOfLex(currentLex, TypeOfLex.LEX_THEN);
            getNextLex();
            parseSentences();
            pl3 = poliz.size();
            poliz.add(new Lex());
            poliz.add(new Lex(TypeOfLex.POLIZ_GO));
            poliz.set(pl2, new Lex(TypeOfLex.POLIZ_LABEL, poliz.size()));
            checkTypeOfLex(currentLex, TypeOfLex.LEX_ELSE);
            getNextLex();
            parseSentences();
            poliz.set(pl3, new Lex(TypeOfLex.POLIZ_LABEL, poliz.size()));
        }//end if
        else if (currentLexType == TypeOfLex.LEX_WHILE) {
            pl0 = poliz.size();
            getNextLex();
            parseExpression();
            eqBool();
            pl1 = poliz.size();
            poliz.add(new Lex());
            poliz.add(new Lex(TypeOfLex.POLIZ_FGO));
            checkTypeOfLex(currentLex, TypeOfLex.LEX_DO);
            getNextLex();
            parseSentences();
            poliz.add(new Lex(TypeOfLex.POLIZ_LABEL, pl0));
            poliz.add(new Lex(TypeOfLex.POLIZ_GO));
            poliz.set(pl1, new Lex(TypeOfLex.POLIZ_LABEL, poliz.size()));
        }//end while
        else if (currentLexType == TypeOfLex.LEX_READ) {
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_ID);
            checkIdInRead();
            poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, currentLexVal));
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            poliz.add(new Lex(TypeOfLex.LEX_READ));
        }//end read
        else if (currentLexType == TypeOfLex.LEX_GET) {
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_ID);
            checkIdInRead();
            poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, currentLexVal));
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_COMMA);
            currentLexVal = glRestArg();
            poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, currentLexVal));
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            poliz.add(new Lex(TypeOfLex.LEX_GET));
        }//end get
        else if (currentLexType == TypeOfLex.LEX_SPINCUBE) {
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            parseExpression();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_COMMA);
            getNextLex();
            parseExpression();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_COMMA);
            getNextLex();
            parseExpression();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_COMMA);
            getNextLex();
            parseExpression();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            poliz.add(new Lex(TypeOfLex.LEX_SPINCUBE));
        }//end spincube
        else if (currentLexType == TypeOfLex.LEX_WRITE) {
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            parseExpression();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            poliz.add(new Lex(TypeOfLex.LEX_WRITE));
        }//end write
        else if (currentLexType == TypeOfLex.LEX_ID) {
            checkId();
            poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, currentLexVal));
            getNextLex();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_ASSIGN);
            ++idCnt;
            getNextLex();
            while (currentLexType == TypeOfLex.LEX_ID) {
                checkId();
                scan.store_pos();
                previousLexVal = currentLexVal;
                previousLexType = currentLexType;
                getNextLex();
                if (currentLexType == TypeOfLex.LEX_ASSIGN) {
                    eqType();
                    if (StatD.TID.get(previousLexVal).get_declare())
                        stackTypeOfLex.push(StatD.TID.get(previousLexVal).get_type());
                    else
                        throw new RuntimeException("not declared");
                    poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, previousLexVal));
                    getNextLex();
                    ++idCnt;
                    continue;
                }
                scan.restore_pos();
                currentLexVal = previousLexVal;
                currentLexType = previousLexType;
                break;
            }
            parseExpression();
            eqType();
            while (idCnt > 0) {
                --idCnt;
                poliz.add(new Lex(TypeOfLex.LEX_ASSIGN));
            }
            if (currentLexType == TypeOfLex.LEX_SEMICOLON) {
                poliz.add(new Lex(TypeOfLex.LEX_SEMICOLON));
            }
        }//assign-end
        else
            parseProgramBody();
    }

    private void parseExpression() {
        parseExpressionPart();
        if (currentLexType == TypeOfLex.LEX_EQ || currentLexType == TypeOfLex.LEX_LSS || currentLexType == TypeOfLex.LEX_GTR ||
                currentLexType == TypeOfLex.LEX_LEQ || currentLexType == TypeOfLex.LEX_GEQ || currentLexType == TypeOfLex.LEX_NEQ) {
            stackTypeOfLex.push(currentLexType);
            getNextLex();
            parseExpressionPart();
            checkOp();
        }
    }

    private void parseExpressionPart() {
        parseTerm();
        while (currentLexType == TypeOfLex.LEX_PLUS || currentLexType == TypeOfLex.LEX_MINUS || currentLexType == TypeOfLex.LEX_OR) {
            stackTypeOfLex.push(currentLexType);
            getNextLex();
            parseTerm();
            checkOp();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (currentLexType == TypeOfLex.LEX_TIMES || currentLexType == TypeOfLex.LEX_SLASH || currentLexType == TypeOfLex.LEX_AND) {
            stackTypeOfLex.push(currentLexType);
            getNextLex();
            parseFactor();
            checkOp();
        }
    }

    private void parseFactor() {
        if (currentLexType == TypeOfLex.LEX_ID) {
            checkId();
            poliz.add(new Lex(TypeOfLex.LEX_ID, currentLexVal));
            getNextLex();
        } else if (currentLexType == TypeOfLex.LEX_NUM) {
            stackTypeOfLex.push(TypeOfLex.LEX_INT);
            poliz.add(currentLex);
            getNextLex();
        } else if (currentLexType == TypeOfLex.LEX_TRUE) {
            stackTypeOfLex.push(TypeOfLex.LEX_BOOL);
            poliz.add(new Lex(TypeOfLex.LEX_TRUE, 1));
            getNextLex();
        } else if (currentLexType == TypeOfLex.LEX_FALSE) {
            stackTypeOfLex.push(TypeOfLex.LEX_BOOL);
            poliz.add(new Lex(TypeOfLex.LEX_FALSE, 0));
            getNextLex();
        } else if (currentLexType == TypeOfLex.LEX_NOT) {
            getNextLex();
            parseFactor();
            checkNot();
        } else if (currentLexType == TypeOfLex.LEX_LPAREN) {
            getNextLex();
            parseExpression();
            checkTypeOfLex(currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
        } else
            throw new RuntimeException(currentLex.toString());
    }

    private void updateTID(TypeOfLex type) {
        int i;
        while (stackInteger.size() > 0) {
            i = StatD.fromStack(stackInteger);
            if (StatD.TID.get(i).get_declare())
                throw new RuntimeException("twice");
            else {
                StatD.TID.get(i).put_declare();
                StatD.TID.get(i).put_type(type);
            }
        }
    }

    private void checkId() {
        if (StatD.TID.get(currentLexVal).get_declare())
            stackTypeOfLex.push(StatD.TID.get(currentLexVal).get_type());
        else
            throw new RuntimeException("not declared");
    }

    private void checkOp() {
        TypeOfLex t1, t2, op, t = TypeOfLex.LEX_INT, r = TypeOfLex.LEX_BOOL;

        t2 = StatD.fromStack(stackTypeOfLex);
        op = StatD.fromStack(stackTypeOfLex);
        t1 = StatD.fromStack(stackTypeOfLex);

        if (op == TypeOfLex.LEX_PLUS || op == TypeOfLex.LEX_MINUS || op == TypeOfLex.LEX_TIMES || op == TypeOfLex.LEX_SLASH)
            r = TypeOfLex.LEX_INT;
        if (op == TypeOfLex.LEX_OR || op == TypeOfLex.LEX_AND)
            t = TypeOfLex.LEX_BOOL;
        if (t1 == t2 && t1 == t)
            stackTypeOfLex.push(r);
        else
            throw new RuntimeException("wrong types are in operation");
        poliz.add(new Lex(op));
    }

    private void checkNot() {
        if (stackTypeOfLex.peek() != TypeOfLex.LEX_BOOL)
            throw new RuntimeException("wrong type is in not");
        else
            poliz.add(new Lex(TypeOfLex.LEX_NOT));
    }

    private void eqType() {
        TypeOfLex typeOfLex;
        typeOfLex = StatD.fromStack(stackTypeOfLex);
        if (typeOfLex != stackTypeOfLex.peek())
            throw new RuntimeException("wrong types are in :=");
        stackTypeOfLex.pop();
    }

    private void eqBool() {
        if (stackTypeOfLex.peek() != TypeOfLex.LEX_BOOL)
            throw new RuntimeException("expression is not boolean");
        stackTypeOfLex.pop();
    }

    private void checkIdInRead() {
        if (!StatD.TID.get(currentLexVal).get_declare())
            throw new RuntimeException("not declared");
    }

    private void getNextLex() {
        currentLex = scan.get_lex();
        currentLexType = currentLex.getTypeOfLex();
        currentLexVal = currentLex.getValueOfLex();
        System.out.println("gl");
        System.out.println(currentLexType);
        System.out.println(currentLexVal);
    }

    private int glRestArg() {
        int restArg = scan.getRestArg();
        return restArg;
    }

    private void checkTypeOfLex(Lex lex, TypeOfLex typeOfLex) {
        if (lex.getTypeOfLex() != typeOfLex) {
            throw new RuntimeException(lex.toString());
        }
    }
}

