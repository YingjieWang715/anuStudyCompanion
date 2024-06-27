package au.edu.anu.comp6442.group03.studyapp.note.search.expression;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.TokenType;

public class ContentExp extends Exp {
    public ContentExp(String value) {
        super(TokenType.CONTENT, value);
    }

    @Override
    public String parse() {
        return this.value;
    }

    @Override
    public boolean match(NoteData note) {
        return note.getContent().replaceAll("\\s+","").toLowerCase().contains(
                this.parse().replaceAll("\\S+","").toLowerCase());
    }
}
