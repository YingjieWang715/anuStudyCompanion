package au.edu.anu.comp6442.group03.studyapp.note.search.expression;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.TokenType;

public class TitleExp extends Exp {
    public TitleExp(String value) {
        super(TokenType.TITLE, value);
    }

    @Override
    public String parse() {
        // Ensure title is not empty and remove any extraneous whitespace
        if (this.value.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        return this.value;
    }

    @Override
    public boolean match(NoteData note) {
        //exact match
        String prcTitle = this.parse().replaceAll("\\s+","").toLowerCase();
        return note.getTitle().replaceAll("\\s+","").toLowerCase().contains(prcTitle);
        //return false;
    }
}
