package au.edu.anu.comp6442.group03.studyapp.note.search;

import java.util.List;

import au.edu.anu.comp6442.group03.studyapp.note.search.expression.Exp;

public class SearchParsingService {
    public List<Exp> parse(String searchText) {
        Tokenizer tokenizer = new Tokenizer(searchText);
        List<Token> tokens = tokenizer.tokenize();
        return new Parser(tokens).parseQuery();
    }
}
