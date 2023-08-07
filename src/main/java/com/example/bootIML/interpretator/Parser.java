package com.example.bootIML.interpretator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Parser {

    SourceProgram sourceProgram;
    Scanner scan;

    public Parser(SourceProgram sourceProgram) {
        this.sourceProgram = sourceProgram;
        scan = new Scanner(sourceProgram);
    }

    public void analyze() {
        getNextLex();
        parseProgram();
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_FIN);

        log.debug("sourceProgram.poliz");
        for (Lex l : sourceProgram.poliz) {
            log.debug(l.toString());
            log.debug(l.typeOfLex.toString() + " " + l.valueOfLex);
        }
        log.info("Parser worked successfully");
    }

    private void parseProgram() {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_PROGRAM);
        getNextLex();
        parseDefinition();
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_SEMICOLON);
        getNextLex();
        parseProgramBody();
    }

    private void parseDefinition() {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_VAR);
        getNextLex();
        parseDefinitionOneType();
        while (sourceProgram.currentLexType == TypeOfLex.LEX_COMMA) {
            getNextLex();
            parseDefinitionOneType();
        }
    }

    private void parseDefinitionOneType() {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
        sourceProgram.stackInteger.push(sourceProgram.currentLexVal);
        getNextLex();
        while (sourceProgram.currentLexType == TypeOfLex.LEX_COMMA) {
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
            sourceProgram.stackInteger.push(sourceProgram.currentLexVal);
            getNextLex();
        }
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COLON);
        getNextLex();
        if (sourceProgram.currentLexType == TypeOfLex.LEX_INT) {
            updateTID(TypeOfLex.LEX_INT);
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_BOOL) {
            updateTID(TypeOfLex.LEX_BOOL);
        } else {
            throw new RuntimeException(sourceProgram.currentLex.toString());
        }
        getNextLex();
    }

    private void parseProgramBody() {
        checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_BEGIN);
        getNextLex();
        parseSentences();
        while (sourceProgram.currentLexType == TypeOfLex.LEX_SEMICOLON) {
            getNextLex();
            parseSentences();
        }
        if (sourceProgram.currentLexType == TypeOfLex.LEX_END) {
            getNextLex();
        } else {
            throw new RuntimeException(sourceProgram.currentLex.toString());
        }
    }

    private void parseSentences() {
        int pl0, pl1, pl2, pl3;
        int idCnt = 0;
        int previousLexVal;
        TypeOfLex previousLexType;

        if (sourceProgram.currentLexType == TypeOfLex.LEX_IF) {
            getNextLex();
            parseExpression();
            eqBool();
            pl2 = sourceProgram.poliz.size();
            sourceProgram.poliz.add(new Lex());
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_FGO));
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_THEN);
            getNextLex();
            parseSentences();
            pl3 = sourceProgram.poliz.size();
            sourceProgram.poliz.add(new Lex());
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_GO));
            sourceProgram.poliz.set(pl2, new Lex(TypeOfLex.POLIZ_LABEL, sourceProgram.poliz.size()));
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ELSE);
            getNextLex();
            parseSentences();
            sourceProgram.poliz.set(pl3, new Lex(TypeOfLex.POLIZ_LABEL, sourceProgram.poliz.size()));
        }//end if
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_WHILE) {
            pl0 = sourceProgram.poliz.size();
            getNextLex();
            parseExpression();
            eqBool();
            pl1 = sourceProgram.poliz.size();
            sourceProgram.poliz.add(new Lex());
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_FGO));
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_DO);
            getNextLex();
            parseSentences();
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_LABEL, pl0));
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_GO));
            sourceProgram.poliz.set(pl1, new Lex(TypeOfLex.POLIZ_LABEL, sourceProgram.poliz.size()));
        }//end while
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_READ) {
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
            checkIdInRead();
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_READ));
        }//end read
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_GET) {
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ID);
            checkIdInRead();
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            sourceProgram.currentLexVal = glRestArg();
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_GET));
        }//end get
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_SPINCUBE) {
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            parseExpression();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            getNextLex();
            parseExpression();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            getNextLex();
            parseExpression();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_COMMA);
            getNextLex();
            parseExpression();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_SPINCUBE));
        }//end spincube
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_WRITE) {
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_LPAREN);
            getNextLex();
            parseExpression();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_WRITE));
        }//end write
        else if (sourceProgram.currentLexType == TypeOfLex.LEX_ID) {
            checkId();
            sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, sourceProgram.currentLexVal));
            getNextLex();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_ASSIGN);
            ++idCnt;
            getNextLex();
            while (sourceProgram.currentLexType == TypeOfLex.LEX_ID) {
                checkId();
                scan.storePos();
                previousLexVal = sourceProgram.currentLexVal;
                previousLexType = sourceProgram.currentLexType;
                getNextLex();
                if (sourceProgram.currentLexType == TypeOfLex.LEX_ASSIGN) {
                    eqType();
                    if (sourceProgram.TID.get(previousLexVal).get_declare())
                        sourceProgram.stackTypeOfLex.push(sourceProgram.TID.get(previousLexVal).get_type());
                    else
                        throw new RuntimeException("not declared");
                    sourceProgram.poliz.add(new Lex(TypeOfLex.POLIZ_ADDRESS, previousLexVal));
                    getNextLex();
                    ++idCnt;
                    continue;
                }
                scan.restorePos();
                sourceProgram.currentLexVal = previousLexVal;
                sourceProgram.currentLexType = previousLexType;
                break;
            }
            parseExpression();
            eqType();
            while (idCnt > 0) {
                --idCnt;
                sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_ASSIGN));
            }
            if (sourceProgram.currentLexType == TypeOfLex.LEX_SEMICOLON) {
                sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_SEMICOLON));
            }
        }//assign-end
        else
            parseProgramBody();
    }

    private void parseExpression() {
        parseExpressionPart();
        if (sourceProgram.currentLexType == TypeOfLex.LEX_EQ || sourceProgram.currentLexType == TypeOfLex.LEX_LSS || sourceProgram.currentLexType == TypeOfLex.LEX_GTR ||
                sourceProgram.currentLexType == TypeOfLex.LEX_LEQ || sourceProgram.currentLexType == TypeOfLex.LEX_GEQ || sourceProgram.currentLexType == TypeOfLex.LEX_NEQ) {
            sourceProgram.stackTypeOfLex.push(sourceProgram.currentLexType);
            getNextLex();
            parseExpressionPart();
            checkOp();
        }
    }

    private void parseExpressionPart() {
        parseTerm();
        while (sourceProgram.currentLexType == TypeOfLex.LEX_PLUS || sourceProgram.currentLexType == TypeOfLex.LEX_MINUS || sourceProgram.currentLexType == TypeOfLex.LEX_OR) {
            sourceProgram.stackTypeOfLex.push(sourceProgram.currentLexType);
            getNextLex();
            parseTerm();
            checkOp();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (sourceProgram.currentLexType == TypeOfLex.LEX_TIMES || sourceProgram.currentLexType == TypeOfLex.LEX_SLASH || sourceProgram.currentLexType == TypeOfLex.LEX_AND) {
            sourceProgram.stackTypeOfLex.push(sourceProgram.currentLexType);
            getNextLex();
            parseFactor();
            checkOp();
        }
    }

    private void parseFactor() {
        if (sourceProgram.currentLexType == TypeOfLex.LEX_ID) {
            checkId();
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_ID, sourceProgram.currentLexVal));
            getNextLex();
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_NUM) {
            sourceProgram.stackTypeOfLex.push(TypeOfLex.LEX_INT);
            sourceProgram.poliz.add(sourceProgram.currentLex);
            getNextLex();
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_TRUE) {
            sourceProgram.stackTypeOfLex.push(TypeOfLex.LEX_BOOL);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_TRUE, 1));
            getNextLex();
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_FALSE) {
            sourceProgram.stackTypeOfLex.push(TypeOfLex.LEX_BOOL);
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_FALSE, 0));
            getNextLex();
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_NOT) {
            getNextLex();
            parseFactor();
            checkNot();
        } else if (sourceProgram.currentLexType == TypeOfLex.LEX_LPAREN) {
            getNextLex();
            parseExpression();
            checkTypeOfLex(sourceProgram.currentLex, TypeOfLex.LEX_RPAREN);
            getNextLex();
        } else
            throw new RuntimeException(sourceProgram.currentLex.toString());
    }

    private void updateTID(TypeOfLex type) {
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

    private void checkId() {
        if (sourceProgram.TID.get(sourceProgram.currentLexVal).get_declare())
            sourceProgram.stackTypeOfLex.push(sourceProgram.TID.get(sourceProgram.currentLexVal).get_type());
        else
            throw new RuntimeException("not declared");
    }

    private void checkOp() {
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

    private void checkNot() {
        if (sourceProgram.stackTypeOfLex.peek() != TypeOfLex.LEX_BOOL)
            throw new RuntimeException("wrong type is in not");
        else
            sourceProgram.poliz.add(new Lex(TypeOfLex.LEX_NOT));
    }

    private void eqType() {
        TypeOfLex typeOfLex;
        typeOfLex = sourceProgram.stackTypeOfLex.remove();
        if (typeOfLex != sourceProgram.stackTypeOfLex.peek())
            throw new RuntimeException("wrong types are in :=");
        sourceProgram.stackTypeOfLex.pop();
    }

    private void eqBool() {
        if (sourceProgram.stackTypeOfLex.peek() != TypeOfLex.LEX_BOOL)
            throw new RuntimeException("expression is not boolean");
        sourceProgram.stackTypeOfLex.pop();
    }

    private void checkIdInRead() {
        if (!sourceProgram.TID.get(sourceProgram.currentLexVal).get_declare())
            throw new RuntimeException("not declared");
    }

    private void getNextLex() {
        sourceProgram.currentLex = scan.get_lex();
        sourceProgram.currentLexType = sourceProgram.currentLex.getTypeOfLex();
        sourceProgram.currentLexVal = sourceProgram.currentLex.getValueOfLex();
        log.debug("gl " + sourceProgram.currentLexType + " " + sourceProgram.currentLexVal);
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

