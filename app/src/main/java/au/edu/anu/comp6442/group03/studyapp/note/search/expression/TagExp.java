package au.edu.anu.comp6442.group03.studyapp.note.search.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;
import au.edu.anu.comp6442.group03.studyapp.note.search.Token;
import au.edu.anu.comp6442.group03.studyapp.note.search.TokenType;

public class TagExp extends Exp {
    public TagExp(String value) {
        super(TokenType.TAG, value);
    }

    @Override
    public List<String> parse() {
        return Arrays.asList(this.value.split(",\\s"));
    }

    @Override
    public boolean match(NoteData note) {
        List<String> parsedTags = this.parse();
        List<String> noteTags = note.getTags();
        List<String> prcNoteTags = new ArrayList<>();
        for (String noteTag : noteTags){
            prcNoteTags.add(noteTag.replaceAll("\\s+","").toLowerCase());
        }
        boolean match = false;
        for (String tag : parsedTags){
            if (prcNoteTags.contains(tag.toLowerCase())){
                match = true;
            }
        }
        return match;
    }
}
