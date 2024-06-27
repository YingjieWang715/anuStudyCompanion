package au.edu.anu.comp6442.group03.studyapp.note.search.expression;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.TokenType;

public abstract class Exp {
    public TokenType type;
    public String value;

    public Exp(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public abstract Object parse();
    public abstract boolean match(NoteData note);
}
