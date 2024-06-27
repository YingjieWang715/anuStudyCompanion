package au.edu.anu.comp6442.group03.studyapp.note.data.io;

import com.google.firebase.Timestamp;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import au.edu.anu.comp6442.group03.studyapp.Utility;
import au.edu.anu.comp6442.group03.studyapp.note.data.NoteData;

public class XmlNoteDataReader implements NoteDataReader {
    @Override
    public List<NoteData> readData(InputStream inputStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        NoteDataHandler handler = new NoteDataHandler();
        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(inputStream));
        return handler.getNotes();
    }

    private static class NoteDataHandler extends DefaultHandler {
        private List<NoteData> notes = new ArrayList<>();
        private NoteData currentNote;
        private StringBuilder builder = new StringBuilder();

        public List<NoteData> getNotes() {
            return notes;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            builder.setLength(0);
            if ("NoteData".equalsIgnoreCase(qName)) {
                currentNote = new NoteData();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case "title":
                    currentNote.setTitle(builder.toString());
                    break;
                case "content":
                    currentNote.setContent(builder.toString());
                    break;
                case "timestamp":
                case "reminder":
                    try {
                        Date date = Utility.parseIso8601Date(builder.toString());
                        Timestamp timestamp = new Timestamp(date);
                        if ("timestamp".equalsIgnoreCase(qName)) {
                            currentNote.setTimestamp(timestamp);
                        } else {
                            currentNote.setReminder(timestamp);
                        }
                    } catch (ParseException e) {
                        System.err.println("Error parsing date: " + builder.toString());
                    }
                    break;
                case "tag":
                    currentNote.addTag(builder.toString());
                    break;
                case "NoteData":
                    notes.add(currentNote);
                    break;
            }
            builder.setLength(0);
        }
    }
}
