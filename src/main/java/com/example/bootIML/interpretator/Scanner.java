package com.example.bootIML.interpretator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Scanner {
    String TW[] = { "", "and", "begin", "bool", "do", "else", "end", "if", "false", "int", "not", "or", "program",
            "read", "then", "true", "var", "while", "write", "get", "spincube"};
    String TD[] = { "@", ";", ",", ":", ":=", "(", ")", "=", "<", ">", "+", "-", "*", "/", "<=", "!=", ">="};

    int look(String buf, String [] list) {
        int i = 0;
        while (i < list.length) {
        	if (buf.equals(list[i]))
                return i;
            ++i;
        }
        return 0;
    }

    int put(SourceProgram sourceProgram, String buf) {
        int k = 0;
        for (Ident l : sourceProgram.TID) {
        	if (buf.equals(l.name))
        		return k;
        	++k;
        }
        sourceProgram.TID.add(new Ident(buf));
        return sourceProgram.TID.size() - 1;
    }
    
    TypeOfLex GetTypeOfOrd (int ord) {
    	int k = 0;
		for (TypeOfLex e: TypeOfLex.values()) {
			if (k == ord)
				return e;
			++k;
		}
		return TypeOfLex.LEX_NULL;
    }
    
    int storePos(SourceProgram sourceProgram) {
        return sourceProgram.getCurrentPos();
    }
    
    void restorePos(SourceProgram sourceProgram, int savedPos) {
        sourceProgram.setCurrentPos(savedPos);
    }
    
    Lex get_lex(SourceProgram sourceProgram) {
        int         d = 1, j;
        char currentChar;
        String      buf = "";
        StateLex CS = StateLex.H;
        do {
            currentChar = sourceProgram.getNextChar();
            switch (CS) {
            case H:
                if (currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t');
                else if (Character.isLetter(currentChar)) {
                    buf = buf + currentChar;
                    CS = StateLex.IDENT;
                }
                else if (Character.isDigit(currentChar)) {
                    d = currentChar - '0';
                    CS = StateLex.NUMB;
                }
                else if (currentChar == '{') {
                    CS = StateLex.COM;
                }
                else if (currentChar == ':' || currentChar == '<' || currentChar == '>') {
                    buf = buf + currentChar;
                    CS = StateLex.ALE;
                }
                else if (currentChar == '@')
                    return new Lex(TypeOfLex.LEX_FIN);
                else if (currentChar == '!') {
                    buf = buf + currentChar;
                    CS = StateLex.NEQ;
                }
                else {
                    buf = buf + currentChar;
                    j = look (buf, TD);                  
                    if (j > 0) {      
                        return new Lex(GetTypeOfOrd(j + TypeOfLex.LEX_FIN.ordinal()), j);
                    }
                    else
                         throw new RuntimeException (String.valueOf(currentChar));
                }
                break;
            case IDENT:
                if (Character.isLetterOrDigit(currentChar)) {
                    buf = buf + currentChar;
                }
                else {
                    sourceProgram.unGetChar();
                    j = look(buf, TW);
                    if (j > 0) {
                          return new Lex(GetTypeOfOrd(j), j);
                    }
                    else {
                        j = put(sourceProgram, buf);
                        return new Lex(TypeOfLex.LEX_ID, j);
                    }
                }
                break;
            case NUMB:
                if (Character.isDigit(currentChar)) {
                    d = d * 10 + (currentChar - '0');
                }
                else {
                    sourceProgram.unGetChar();
                    return new Lex(TypeOfLex.LEX_NUM, d);
                }
                break;
            case COM:
                if (currentChar == '}') {
                    CS = StateLex.H;
                }
                else if (currentChar == '@' || currentChar == '{')
                	throw new RuntimeException (String.valueOf(currentChar));
                break;
            case ALE:
                if (currentChar == '=') {
                    buf = buf + currentChar;
                }
                else {
                    sourceProgram.unGetChar();
                }
                j = look(buf, TD);
                return new Lex(GetTypeOfOrd(j + TypeOfLex.LEX_FIN.ordinal()), j);
            case NEQ:
                if (currentChar == '=') {
                    buf = buf + currentChar;
                    j = look(buf, TD);
                    return new Lex(TypeOfLex.LEX_NEQ, j);
                }
                else
                	throw new RuntimeException (String.valueOf('!'));
             } //end switch
        } while (true);
    }

    int getRestArg(SourceProgram sourceProgram) {
        char currentChar;
        String buf = "";
         do {
            currentChar = sourceProgram.getNextChar();
            if (currentChar == ')') {
                log.debug("getRestArg: " + buf);
                sourceProgram.unGetChar();
                sourceProgram.restArg.add(buf);
                return sourceProgram.restArg.size() - 1;
              }
            else {
                buf = buf + currentChar;
            }
        } while (true);
    }
}

