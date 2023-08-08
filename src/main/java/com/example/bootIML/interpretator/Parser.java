package com.example.bootIML.interpretator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Parser {

    private final Scanner scanner;

    public void analyze(SourceProgram sourceProgram) {
        getNextLex(sourceProgram);
        parseProgram(sourceProgram);
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_FIN);

        log.debug("sourceProgram.poliz");
        for (Lex l : sourceProgram.poliz) {
            log.debug(l.toString());
            log.debug(l.typeOfLex.toString() + " " + l.valueOfLex);
        }
        log.info("Parser worked successfully");
    }

    private void parseProgram(SourceProgram sourceProgram) {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_PROGRAM);
        getNextLex(sourceProgram);
        parseDefinition(sourceProgram);
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_SEMICOLON);
        getNextLex(sourceProgram);
        parseProgramBody(sourceProgram);
    }

    private void parseDefinition(SourceProgram sourceProgram) {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_VAR);
        getNextLex(sourceProgram);
        parseDefinitionOneType(sourceProgram);
        while (sourceProgram.currentLexType == TypeOfLex.LEX_COMMA) {
            getNextLex(sourceProgram);
            parseDefinitionOneType(sourceProgram);
        }
    }

    private void parseDefinitionOneType(SourceProgram sourceProgram) {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
        sourceProgram.stackInteger.push(sourceProgram.currentLexVal);
        getNextLex(sourceProgram);
        while (sourceProgram.currentLexType == TypeOfLex.LEX_COMMA) {
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
            sourceProgram.stackInteger.push(sourceProgram.currentLexVal);
            getNextLex(sourceProgram);
        }
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COLON);
        getNextLex(sourceProgram);
        if (sourceProgram.currentLexType == TypeOfLex.LEX_INT) {
            updateTID(sourceProgram, TypeOfLex.LEX_INT);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_BOOL) {
            updateTID(sourceProgram, TypeOfLex.LEX_BOOL);
        } else {
            throw new RuntimeException(sourceProgram.currentLex.toString());
        }
        getNextLex(sourceProgram);
    }

    private void parseProgramBody(SourceProgram sourceProgram) {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_BEGIN);
        getNextLex(sourceProgram);
        parseSentences(sourceProgram);
        while (sourceProgram.currentLexType == TypeOfLex.LEX_SEMICOLON) {
            getNextLex(sourceProgram);
            parseSentences(sourceProgram);
        }
        if (sourceProgram.currentLexType == TypeOfLex.LEX_END) {
            getNextLex(sourceProgram);
        } else {
            throw new RuntimeException(sourceProgram.currentLex.toString());
        }
    }

    private void parseSentences(SourceProgram sourceProgram) {
        int pl0, pl1, pl2, pl3;
        int idCnt = 0;
        int previousLexVal;
        int savedPos;
        TypeOfLex previousLexType;

        if (sourceProgram.currentLexType == TypeOfLex.LEX_IF) {
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            eqBool(sourceProgram);
            pl2 = sourceProgram.poliz.size();
            sourceProgram.poliz.add(new Lex());
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_FGO));
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_THEN);
            getNextLex(sourceProgram);
            parseSentences(sourceProgram);
            pl3 = sourceProgram.poliz.size();
            sourceProgram.poliz.add(new Lex());
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_GO));
            sourceProgram.poliz.set(pl2, new Lex(TypeOfLex.POLIZ_LABEL, sourceProgram.poliz.size()));
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ELSE);
            getNextLex(sourceProgram);
            parseSentences(sourceProgram);
            sourceProgram.poliz.set(pl3, new Lex(TypeOfLex.POLIZ_LABEL, sourceProgram.poliz.size()));
        }//end if
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_WHILE) {
            pl0 = sourceProgram.poliz.size();
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            eqBool(sourceProgram);
            pl1 = sourceProgram.poliz.size();
            sourceProgram.poliz.add(new Lex());
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_FGO));
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_DO);
            getNextLex(sourceProgram);
            parseSentences(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_LABEL, pl0));
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_GO));
            sourceProgram.poliz.set(pl1, new Lex(TypeOfLex.POLIZ_LABEL, sourceProgram.poliz.size()));
        }//end while
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_READ) {
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
            checkIdInRead(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_READ));
        }//end read
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_GET) {
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
            checkIdInRead(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            sourceProgram.currentLexVal = glRestArg(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_GET));
        }//end get
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_SPINCUBE) {
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_SPINCUBE));
        }//end spincube
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_WRITE) {
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_WRITE));
        }//end write
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_ID) {
            checkId(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ASSIGN);
            ++idCnt;
            getNextLex(sourceProgram);
            while (sourceProgram.currentLexType == TypeOfLex.LEX_ID) {
                checkId(sourceProgram);
                savedPos = scanner.storePos(sourceProgram);
                previousLexVal = sourceProgram.currentLexVal;
                previousLexType = sourceProgram.currentLexType;
                getNextLex(sourceProgram);
                if (sourceProgram.currentLexType == TypeOfLex.LEX_ASSIGN) {
                    eqType(sourceProgram);
                    if (sourceProgram.TID.get(previousLexVal).get_declare())
                        sourceProgram.stackTypeOfLex.push(sourceProgram.TID.get(previousLexVal).get_type());
                    else
                        throw new RuntimeException("not declared");
                    sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, previousLexVal));
                    getNextLex(sourceProgram);
                    ++idCnt;
                    continue;
                }
                scanner.restorePos(sourceProgram, savedPos);
                sourceProgram.currentLexVal = previousLexVal;
                sourceProgram.currentLexType = previousLexType;
                break;
            }
            parseExpression(sourceProgram);
            eqType(sourceProgram);
            while (idCnt > 0) {
                --idCnt;
                sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_ASSIGN));
            }
            if (sourceProgram.currentLexType == TypeOfLex.LEX_SEMICOLON) {
                sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_SEMICOLON));
            }
        }//assign-end
        else
            parseProgramBody(sourceProgram);
    }

    private void parseExpression(SourceProgram sourceProgram) {
        parseExpressionPart(sourceProgram);
        if (sourceProgram.currentLexType == TypeOfLex.LEX_EQ || sourceProgram.currentLexType == TypeOfLex.LEX_LSS || sourceProgram.currentLexType == TypeOfLex.LEX_GTR ||
                sourceProgram.currentLexType == TypeOfLex.LEX_LEQ || sourceProgram.currentLexType == TypeOfLex.LEX_GEQ || sourceProgram.currentLexType == TypeOfLex.LEX_NEQ) {
            sourceProgram.stackTypeOfLex.push(sourceProgram.currentLexType);
            getNextLex(sourceProgram);
            parseExpressionPart(sourceProgram);
            checkOp(sourceProgram);
        }
    }

    private void parseExpressionPart(SourceProgram sourceProgram) {
        parseTerm(sourceProgram);
        while (sourceProgram.currentLexType == TypeOfLex.LEX_PLUS || sourceProgram.currentLexType == TypeOfLex.LEX_MINUS || sourceProgram.currentLexType == TypeOfLex.LEX_OR) {
            sourceProgram.stackTypeOfLex.push(sourceProgram.currentLexType);
            getNextLex(sourceProgram);
            parseTerm(sourceProgram);
            checkOp(sourceProgram);
        }
    }

    private void parseTerm(SourceProgram sourceProgram) {
        parseFactor(sourceProgram);
        while (sourceProgram.currentLexType == TypeOfLex.LEX_TIMES || sourceProgram.currentLexType == TypeOfLex.LEX_SLASH || sourceProgram.currentLexType == TypeOfLex.LEX_AND) {
            sourceProgram.stackTypeOfLex.push(sourceProgram.currentLexType);
            getNextLex(sourceProgram);
            parseFactor(sourceProgram);
            checkOp(sourceProgram);
        }
    }

    private void parseFactor(SourceProgram sourceProgram) {
        if (sourceProgram.currentLexType == TypeOfLex.LEX_ID) {
            checkId(sourceProgram);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_ID, sourceProgram.currentLexVal));
            getNextLex(sourceProgram);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_NUM) {
            sourceProgram.stackTypeOfLex.push(TypeOfLex.LEX_INT);
            sourceProgram.poliz.add(sourceProgram.currentLex);
            getNextLex(sourceProgram);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_TRUE) {
            sourceProgram.stackTypeOfLex.push(TypeOfLex.LEX_BOOL);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_TRUE, 1));
            getNextLex(sourceProgram);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_FALSE) {
            sourceProgram.stackTypeOfLex.push(TypeOfLex.LEX_BOOL);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_FALSE, 0));
            getNextLex(sourceProgram);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_NOT) {
            getNextLex(sourceProgram);
            parseFactor(sourceProgram);
            checkNot(sourceProgram);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_LPAREN) {
            getNextLex(sourceProgram);
            parseExpression(sourceProgram);
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex(sourceProgram);
        } else
            throw new RuntimeException(sourceProgram.currentLex.toString());
    }

    private void updateTID(SourceProgram sourceProgram,TypeOfLex type) {
        int i;
        while (sourceProgram.stackInteger.size() > 0) {
            i = sourceProgram.stackInteger.remove();
            if (sourceProgram.TID.get(i).get_declare())
                throw new RuntimeException("twice");
            else {
                sourceProgram.TID.get(i).put_declare();
                sourceProgram.TID.get(i).put_type(type);
            }
        }
    }

    private void checkId(SourceProgram sourceProgram) {
        if (sourceProgram.TID.get(sourceProgram.currentLexVal).get_declare())
            sourceProgram.stackTypeOfLex.push(sourceProgram.TID.get(sourceProgram.currentLexVal).get_type());
        else
            throw new RuntimeException("not declared");
    }

    private void checkOp(SourceProgram sourceProgram) {
        TypeOfLex t1, t2, op, t = TypeOfLex.LEX_INT, r = TypeOfLex.LEX_BOOL;

        t2 = sourceProgram.stackTypeOfLex.remove();
        op = sourceProgram.stackTypeOfLex.remove();
        t1 = sourceProgram.stackTypeOfLex.remove();

        if (op == TypeOfLex.LEX_PLUS || op == TypeOfLex.LEX_MINUS || op == TypeOfLex.LEX_TIMES || op == TypeOfLex.LEX_SLASH)
            r = TypeOfLex.LEX_INT;
        if (op == TypeOfLex.LEX_OR || op == TypeOfLex.LEX_AND)
            t = TypeOfLex.LEX_BOOL;
        if (t1 == t2 && t1 == t)
            sourceProgram.stackTypeOfLex.push(r);
        else
            throw new RuntimeException("wrong types are in operation");
        sourceProgram.poliz.add(new Lex(op));
    }

    private void checkNot(SourceProgram sourceProgram) {
        if (sourceProgram.stackTypeOfLex.peek() != TypeOfLex.LEX_BOOL)
            throw new RuntimeException("wrong type is in not");
        else
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_NOT));
    }

    private void eqType(SourceProgram sourceProgram) {
        TypeOfLex typeOfLex;
        typeOfLex = sourceProgram.stackTypeOfLex.remove();
        if (typeOfLex != sourceProgram.stackTypeOfLex.peek())
            throw new RuntimeException("wrong types are in :=");
        sourceProgram.stackTypeOfLex.pop();
    }

    private void eqBool(SourceProgram sourceProgram) {
        if (sourceProgram.stackTypeOfLex.peek() != TypeOfLex.LEX_BOOL)
            throw new RuntimeException("expression is not boolean");
        sourceProgram.stackTypeOfLex.pop();
    }

    private void checkIdInRead(SourceProgram sourceProgram) {
        if (!sourceProgram.TID.get(sourceProgram.currentLexVal).get_declare())
            throw new RuntimeException("not declared");
    }

    private void getNextLex(SourceProgram sourceProgram) {
        sourceProgram.currentLex = scanner.get_lex(sourceProgram);
        sourceProgram.currentLexType = sourceProgram.currentLex.getTypeOfLex();
        sourceProgram.currentLexVal = sourceProgram.currentLex.getValueOfLex();
        log.debug("gl " + sourceProgram.currentLexType + " " + sourceProgram.currentLexVal);
    }

    private int glRestArg(SourceProgram sourceProgram) {
        int restArg = scanner.getRestArg(sourceProgram);
        return restArg;
    }

    private void checkTypeOfLex(Lex lex, TypeOfLex typeOfLex) {
        if (lex.getTypeOfLex() != typeOfLex) {
            throw new RuntimeException(lex.toString());
        }
    }
}

